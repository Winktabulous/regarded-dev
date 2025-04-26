package com.near_reality.game.content.wilderness.event.ganodermic_beast

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.wilderness.event.WildernessEventManager
import com.zenyte.plugins.events.ServerLaunchEvent

@Suppress("unused")
object GanodermicBeastModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        WildernessEventManager.registerEvent(GanodermicBeastEvent)
    }
}
