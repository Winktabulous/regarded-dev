package com.zenyte.plugins

import com.google.common.base.Stopwatch
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import org.slf4j.LoggerFactory
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

/**
 * Lightning fast plugin loader.
 *
 * @author Jire
 */
object PluginLoader {

    private val log = LoggerFactory.getLogger(PluginLoader::class.java)

    @JvmOverloads
    @JvmStatic
    fun loadFromFilePath(
        vararg pluginTypes: PluginType = PluginType.values,
        filePath: String,
    ) {
        val supportedPluginTypeIDs: IntSet = IntOpenHashSet(pluginTypes.size)
        for (pluginType in pluginTypes) supportedPluginTypeIDs.add(pluginType.id)

        val stopwatch = Stopwatch.createStarted()

        val data: ByteBuffer
        RandomAccessFile(filePath, "r").use { raf ->
            val channel = raf.channel
            data = ByteBuffer.allocateDirect(channel.size().toInt())
            channel.read(data)
        }
        data.flip()

        val loadedPlugins = data.int.toLong() and 0xFFFF_FFFF
        for (i in 1..loadedPlugins) {
            val id = data.get().toInt() and 0xFF
            val pluginType: PluginType = PluginType.forID(id)
                ?: throw UnsupportedOperationException("Unknown plugin type ID $id")

            val pluginClassName = data.readString()

            val supported = supportedPluginTypeIDs.contains(id)
            if (!supported) continue

            try {
                val loadedPluginClass = Class.forName(pluginClassName)
                pluginType.pluginTypeLoader?.loadClass(loadedPluginClass)
            } catch (e: Exception) {
                log.error("Failed to load plugin \"$pluginClassName\"", e)
            }
        }

        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        log.info("Loaded {} plugins in {} ms.", loadedPlugins, elapsed)
    }

    @JvmStatic
    @JvmOverloads
    fun load(
        vararg pluginTypes: PluginType = PluginType.values,
        filePath: String = PluginScanner.DEFAULT_FILE_PATH,
    ) = try {
        loadFromFilePath(*pluginTypes, filePath = filePath)
    } catch (e: Exception) {
        log.error("Failed to load plugins.", e)
    }

    private fun ByteBuffer.readString(): String {
        val sizeBytes = get().toInt() and 0xFF
        val bytes = ByteArray(sizeBytes)
        get(bytes)
        return String(bytes, Charsets.UTF_8)
    }

}