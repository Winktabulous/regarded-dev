package com.near_reality.game.content.wilderness.revenant

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.wilderness.revenant.ForinthrySurge.activate
import com.near_reality.game.content.wilderness.revenant.ForinthrySurge.deactivate
import com.near_reality.game.content.wilderness.revenant.npc.RevenantMaledictus
import com.near_reality.game.world.PlayerEvent
import com.near_reality.game.world.hook
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.entity.player.variables.TickVariable
import com.zenyte.plugins.events.ServerLaunchEvent

@Suppress("unused")
object RevenantModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        registerPlayerDeathHook(event)
        registerCommands()
    }

    private fun registerPlayerDeathHook(event: ServerLaunchEvent) {
        event.worldThread.hook<PlayerEvent.Died> {
            /*
             * If player had Forinthry surge active, attempt to transfer it for the killer
             */
            if (ForinthrySurge.isActive(it.player)) {
                val killer = it.killer
                if (killer is Player)
                    activate(killer)
            }
        }
    }

    private fun registerCommands() {
        Command(PlayerPrivilege.ADMINISTRATOR, "spawnmaledictus") { p: Player?, args: Array<String?>? ->
            RevenantMaledictus.spawn()
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "surge", "Toggles your Forinthry surge status") { p, _ ->
            val time = p.variables.getTime(TickVariable.FORINTHRY_SURGE)
            if (time > 0) {
                deactivate(p)
            } else {
                activate(p)
            }
            p.updateFlags.flag(UpdateFlag.APPEARANCE)
        }
    }
}
