package com.near_reality.tools.backups

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.exists
import kotlin.time.Duration
import kotlin.time.toJavaDuration

abstract class Backup<T>(
    var period: Duration,
    var subfileExtension: String,
    var zipFileSuffix: String,
    private val useCaching: Boolean,
) {

    /**
     * Used for formatting of the file name of the created zip archive.
     */
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss")

    /**
     * If initialised represents the [time][LocalDateTime] at which the last backup was finalised.
     */
    private lateinit var lastBackupTime: LocalDateTime

    /**
     * The logger instance used for backups of this type.
     */
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val cachedBackups = ConcurrentHashMap<String, ByteArray>()

    /**
     * Create a file name (without extension) for the argued [instance].
     */
    abstract fun toName(instance: T): String

    /**
     * Serialize the [instance] to a [ByteArray].
     */
    abstract suspend fun serialise(instance: T): ByteArray

    fun isTimeForNextBackup(time: LocalDateTime) =
        !this::lastBackupTime.isInitialized ||
                time.isAfter(lastBackupTime.plus(period.toJavaDuration()))

    fun makeBackupOf(
        coroutineScope: CoroutineScope,
        backupsDir: Path,
        instances: Collection<T>,
        time: LocalDateTime,
    ) {
        try {
            if (instances.isEmpty()) {
                logger.info("Not making backup because there are no instances to backup.")
            } else {
                logger.info("Submitting request to make {} {} backups.", instances.size, toString())
                coroutineScope.launch {

                    val serializedResults = instances
                        .map { async { trySerialize(it) } }
                        .awaitAll()

                    val failures = serializedResults.filterIsInstance<Result.Failed>()
                    if (failures.isNotEmpty()) {
                        logger.error("{} backups failed", failures.size)
                        for (failure in failures)
                            logger.error("Failed to serialize {}", failure.name, failure.throwable)
                    }

                    val successes = serializedResults.filterIsInstance<Result.Success>()
                    if (successes.isNotEmpty()) {
                        val saveFile = getSaveFile(backupsDir, time)
                        saveFile.outputStream().use {
                            val zipOutputStream = ZipOutputStream(it)
                            for (success in successes) {
                                zipOutputStream.putNextEntry(ZipEntry("${success.name}.$subfileExtension"))
                                zipOutputStream.write(success.encoded)
                                zipOutputStream.closeEntry()
                            }
                            zipOutputStream.close()
                        }
                        logger.info("Wrote backup to {}", saveFile)
                    } else
                        logger.info("Ignoring backup because there are no instances to serialize.")
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to create {} (time={})", this, time)
        } finally {
            lastBackupTime = time
        }
    }

    private suspend fun trySerialize(instance: T): Result {
        val name = toName(instance)
        try {
            val encoded = serialise(instance)
            if (useCaching) {
                if (cachedBackups[name]?.contentEquals(encoded) == true) {
                    logger.debug("Skipping encoding of {} because serialised data is same as cached.", instance)
                    return Result.Cached
                }
                cachedBackups[name] = encoded
            }
            return Result.Success(name, encoded)
        } catch (t: Throwable) {
            return Result.Failed(name, t)
        }
    }

    private sealed class Result {

        object Cached : Result()
        class Success(val name: String, val encoded: ByteArray) : Result()
        class Failed(val name: String, val throwable: Throwable) : Result()
    }

    private fun getSaveFile(backupsDir: Path, time: LocalDateTime): File {
        if (!backupsDir.exists())
            backupsDir.toFile().mkdirs()
        var file: File
        val fileNameWithoutExtension = dateFormatter.format(time)
        var count = 0
        do {
            file =
                backupsDir.resolve("$fileNameWithoutExtension$zipFileSuffix${if (count++ > 0) "_$count" else ""}.zip")
                    .toFile()
        } while (file.exists())
        return file
    }

    override fun toString() =
        "${this::class.simpleName}"
}
