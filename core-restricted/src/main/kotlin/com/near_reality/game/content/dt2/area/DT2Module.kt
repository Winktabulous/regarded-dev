package com.near_reality.game.content.dt2.area

import com.google.common.eventbus.Subscribe
import com.zenyte.plugins.events.ServerLaunchEvent

object DT2Module {
    @JvmStatic
    @Subscribe
    fun onServerLaunch(event: ServerLaunchEvent) {
        DT2Commands.register()
    }
}