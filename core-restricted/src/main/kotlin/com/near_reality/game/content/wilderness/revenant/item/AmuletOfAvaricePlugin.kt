package com.near_reality.game.content.wilderness.revenant.item

import com.near_reality.game.content.wilderness.revenant.ForinthrySurge
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.variables.TickVariable
import com.zenyte.utils.TimeUnit

/**
 * @author Andys1814.
 */
@Suppress("unused")
class AmuletOfAvaricePlugin : ItemPlugin() {

    override fun handle() {
        bind("Check") { player: Player, _, _, _ ->
            if (ForinthrySurge.isActive(player)) {
                val ticks = player.variables.getTime(TickVariable.FORINTHRY_SURGE).toLong()
                val seconds = TimeUnit.TICKS.toSeconds(ticks) % 60
                val minutes = TimeUnit.TICKS.toMinutes(ticks)
                var time = ""
                if (minutes > 0) {
                    time += "$minutes minutes"
                    if (seconds > 0) {
                        time += " and "
                    }
                }
                if (seconds > 0) {
                    time += "$seconds seconds"
                }
                player.sendMessage("You have $time of Forinthry surge remaining.")
            } else {
                player.sendMessage("You don't have Forinthry surge.")
            }
        }
    }

    override fun getItems() = intArrayOf(
        ItemId.AMULET_OF_AVARICE
    )
}
