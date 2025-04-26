package com.zenyte.plugins

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Jire
 */
object InitPluginLoader : PluginTypeLoader {

    private val logger: Logger = LoggerFactory.getLogger(InitPluginLoader::class.java)

    override fun loadClass(pluginClass: Class<*>) {
        try {
            val constructor = pluginClass.getDeclaredConstructor()
            val instance = constructor.newInstance()

            val initPlugin = instance as InitPlugin
            initPlugin.init()
        } catch (e: Exception) {
            logger.error("Failed to init plugin class $pluginClass", e)
        }
    }

}