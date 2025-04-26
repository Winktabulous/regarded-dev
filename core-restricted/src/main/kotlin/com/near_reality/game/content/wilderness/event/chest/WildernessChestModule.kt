package com.near_reality.game.content.wilderness.event.chest

import com.google.common.eventbus.Subscribe
import com.zenyte.plugins.events.ServerLaunchEvent

/**
 * The module that initializes the wilderness chest event.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object WildernessChestModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
//        WildernessEventManager.registerEvent(WildernessChestEvent)
    }
}
