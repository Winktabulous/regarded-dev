@file:Suppress("unused")

package com.near_reality.api

import com.google.common.eventbus.Subscribe
import com.zenyte.plugins.events.ServerLaunchEvent

/**
 * Represents the game hooks for API related activities.
 *
 * @author Stan van der Bend
 */
object APIHooks {

    /**
     * Registers a server launch event hook that will start the api callback server.
     */
    @Subscribe
    @JvmStatic
    fun onServerLaunched(event: ServerLaunchEvent) {
        APICallbackServer.start()
    }
}
