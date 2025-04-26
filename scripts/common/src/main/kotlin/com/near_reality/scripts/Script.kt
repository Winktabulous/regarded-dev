package com.near_reality.scripts

import com.zenyte.plugins.Event
import com.zenyte.plugins.PluginManager

/**
 * @author Jire
 */
interface Script {

    operator fun Event.invoke(handle: Event.() -> Unit) =
        PluginManager.register(javaClass, handle)

}