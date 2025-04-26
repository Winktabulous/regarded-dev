@file:Suppress("UNUSED", "UNUSED_PARAMETER")

package com.near_reality.game.model.item.protection

import com.google.common.eventbus.Subscribe
import com.zenyte.plugins.events.ServerLaunchEvent

object ItemProtectionModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunch(serverLaunchEvent: ServerLaunchEvent) {
        ItemProtectionValueManager.loadProtectionValues()
        ItemProtectionCommands.loadCommands()
    }
}
