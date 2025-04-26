@file:Suppress("unused")

package com.near_reality.tools

import com.google.common.eventbus.Subscribe
import com.near_reality.net.HardwareInfo
import com.near_reality.tools.logging.file.nrJson
import com.zenyte.plugins.events.LoginEvent
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Collectors
import kotlin.io.path.extension
import kotlin.io.path.inputStream
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.outputStream
import kotlin.system.measureTimeMillis

/**
 * Loads [HardwareInfo] instances used to cross-link player accounts.
 *
 * @author Stan van der Bend
 */
object HardwareInfoModule {

    private val logger = LoggerFactory.getLogger(HardwareInfoModule::class.java)
    private val hardwareInfoByPlayer = mutableMapOf<String, MutableSet<HardwareInfo>>()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Gets all the [HardwareInfo] instances for the [username].
     */
    operator fun get(username: String): MutableSet<HardwareInfo> =
        hardwareInfoByPlayer[username] ?: mutableSetOf()

    /**
     * Gets all the usernames with the [hardwareInfo] associated to them.
     */
    fun getUsernamesWithHardware(hardwareInfo: HardwareInfo): Set<String> =
        hardwareInfoByPlayer.filterValues { hardwareInfoSet -> hardwareInfoSet.any { it == hardwareInfo } }.keys

    @Subscribe
    @JvmStatic
    @OptIn(ExperimentalSerializationApi::class)
    fun onServerLaunched(serverLaunchEvent: ServerLaunchEvent) {
        val folder = getPath()
        val duration = measureTimeMillis {
            hardwareInfoByPlayer.putAll(Files.list(folder).filter { it.extension == "json" }.map {
                val hardwareInfo: MutableSet<HardwareInfo> = it.inputStream().use(nrJson::decodeFromStream)
                it.nameWithoutExtension to hardwareInfo
            }.collect(Collectors.toUnmodifiableList()))
        }
        logger.info("Loading {} hardware info instances took {}ms", hardwareInfoByPlayer.size, duration)
    }

    @Subscribe
    @JvmStatic
    fun onPlayerLogin(loginEvent: LoginEvent) {
        val player = loginEvent.player
        val hardwareInfo = player.playerInformation.hardware
        val hardwareInfoForPlayer =
            hardwareInfoByPlayer.getOrPut(player.username) { mutableSetOf() }
        if (hardwareInfoForPlayer.add(hardwareInfo))
            write(hardwareInfoForPlayer, player.username)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @OptIn(ExperimentalSerializationApi::class)
    private fun write(
        hardwareInfoForPlayer: MutableSet<HardwareInfo>,
        username: String,
    ) {
        scope.launch {
            val folder = getPath()
            val fileName = "$username.json"
            val file = folder.resolve(fileName)

            val tempFile = Files.createTempFile(folder, fileName, ".tmp")
            tempFile.outputStream().use {
                nrJson.encodeToStream(hardwareInfoForPlayer, it)
            }
            Files.move(tempFile, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)

            logger.debug("Wrote {} hardware info instances for {} to {}", hardwareInfoForPlayer.size, username, file)
        }
    }

    private fun getPath(): Path {
        val folder = Paths.get("data", "characters-hardware")
        if (Files.notExists(folder))
            Files.createDirectories(folder)
        return folder
    }
}
