package com.zenyte.plugins

import com.google.common.base.Stopwatch
import com.near_reality.NearReality
import com.zenyte.Main
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ClassInfoList
import io.github.classgraph.ScanResult
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import org.slf4j.LoggerFactory
import java.io.RandomAccessFile
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * Use this to update the plugins data file, after a new plugin class has been added to the source.
 *
 * @author Jire
 */
object PluginScanner {

    const val DEFAULT_FILE_PATH = "data/plugins.dat"

    val defaultPackages = arrayOf(
        Main::class.java.packageName,
        NearReality::class.java.packageName,
    )

    private val log = LoggerFactory.getLogger(PluginScanner::class.java)

    @JvmStatic
    @JvmOverloads
    fun createClassGraph(vararg packages: String = defaultPackages): ClassGraph =
        ClassGraph()
            .ignoreClassVisibility()
            .ignoreMethodVisibility()
            .enableAnnotationInfo()
            .enableMethodInfo()
            .enableClassInfo()
            .enableExternalClasses()
            .acceptPackages(*packages)

    @JvmStatic
    @JvmOverloads
    inline fun scanClassGraph(
        vararg packages: String = defaultPackages,
        use: (ScanResult) -> Unit
    ) = createClassGraph(*packages)
        .scan()
        .use(use)

    @JvmStatic
    @JvmOverloads
    inline fun scanClasses(
        vararg pluginTypes: PluginType = PluginType.values,
        packages: Array<String> = defaultPackages,
        use: (PluginType, ClassInfoList) -> Unit
    ) = scanClassGraph(*packages) { result ->
        val skippedPlugins = result.getSkipPluginScanClasses()
        pluginTypes.forEach {
            use(
                it,
                it.scan(result)
                    .exclude(skippedPlugins)
            )
        }
    }

    @JvmStatic
    fun ScanResult.getSkipPluginScanClasses(): ClassInfoList = getClassesWithAnnotation(SkipPluginScan::class.java)

    @JvmStatic
    @JvmOverloads
    fun scan(
        vararg pluginTypes: PluginType = PluginType.values,
        filePath: String = DEFAULT_FILE_PATH,
        packages: Array<String> = defaultPackages
    ) {
        log.info("Scanning for plugins...")

        val stopwatch = Stopwatch.createStarted()

        val plugins: MutableSet<ScannedPlugin> = ObjectOpenHashSet()
        try {
            scanClasses(*pluginTypes, packages = packages) { pluginType, classInfoList ->
                for (classInfo in classInfoList) {
                    try {
                        val plugin = ScannedPlugin(pluginType, classInfo.name, classInfo.priority)
                        if (!plugins.add(plugin)) {
                            throw UnsupportedOperationException("Couldn't add $plugin")
                        }
                        log.info("Scanned plugin {}", plugin)
                    } catch (e: Exception) {
                        log.error("Error adding plugin for $classInfo (type=$pluginType)", e)
                    }
                }
            }

            val buf = Unpooled.directBuffer()
            try {
                for (plugin in plugins.sortedBy { it.priority }) {
                    buf.writeByte(plugin.pluginType.id)
                    buf.writeString(plugin.className)
                }

                val headBuf = Unpooled.directBuffer(4 + buf.readableBytes())
                    .writeInt(plugins.size)
                    .writeBytes(buf)
                try {
                    val nioBuffer = headBuf.nioBuffer()
                    RandomAccessFile(filePath, "rw").use { raf ->
                        raf.channel.write(nioBuffer)
                    }
                } finally {
                    headBuf.release()
                }
            } finally {
                buf.release()
            }
        } catch (e: Exception) {
            log.error("Error during processing Scanner results:", e)
            return
        }

        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        log.info(
            "Successfully scanned {} plugins and wrote to file path \"{}\" in {}ms.",
            plugins.size, filePath, elapsed
        )
    }

    private data class ScannedPlugin(
        val pluginType: PluginType,
        val className: String,
        val priority: Int
    )

    private fun ByteBuf.writeString(string: String) {
        val bytes = string.toByteArray(StandardCharsets.UTF_8)
        require(bytes.size <= 255)

        writeByte(bytes.size)
        writeBytes(bytes)
    }

    @JvmStatic
    fun main(args: Array<String>) = scan()

    val ClassInfo.priority: Int
        get() = getAnnotationInfo(PluginPriority::class.java)
            ?.getParameterValues(true)
            ?.get(0)
            ?.value as? Int
            ?: PluginPriority.DEFAULT

}
