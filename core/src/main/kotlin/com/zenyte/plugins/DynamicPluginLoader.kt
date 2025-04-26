package com.zenyte.plugins

import com.google.common.base.Stopwatch
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * Useful for utility classes, where a `.dat` file is not preferred.
 *
 * @author Jire
 */
object DynamicPluginLoader {

    private val log = LoggerFactory.getLogger(DynamicPluginLoader::class.java)

    @JvmStatic
    @JvmOverloads
    fun loadCounted(
        vararg pluginTypes: PluginType = PluginType.values,
        packages: Array<String> = PluginScanner.defaultPackages
    ): Int {
        var loaded = 0
        PluginScanner.scanClasses(*pluginTypes, packages = packages) { pluginType, classInfoList ->
            val classes = classInfoList.loadClasses()
            pluginType.loadClasses(classes)
            loaded += classes.size
        }
        return loaded
    }

    @JvmStatic
    @JvmOverloads
    fun load(
        vararg pluginTypes: PluginType = PluginType.values,
        packages: Array<String> = PluginScanner.defaultPackages
    ) {
        val stopwatch = Stopwatch.createStarted()
        val loadedPlugins = loadCounted(*pluginTypes, packages = packages)
        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        log.info("Dynamically loaded {} plugins in {} ms.", loadedPlugins, elapsed)
    }

}