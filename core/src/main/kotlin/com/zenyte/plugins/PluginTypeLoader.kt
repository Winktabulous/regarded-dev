package com.zenyte.plugins

/**
 * @author Jire
 */
fun interface PluginTypeLoader {

    fun loadClass(pluginClass: Class<*>)

    fun loadClasses(pluginClasses: Collection<Class<*>>) = pluginClasses.forEach(::loadClass)

}