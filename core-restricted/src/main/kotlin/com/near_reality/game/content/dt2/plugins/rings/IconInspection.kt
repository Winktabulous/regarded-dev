package com.near_reality.game.content.dt2.plugins.rings

import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-09
 */
class IconInspection : ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player, item, _, _ ->
            player.dialogue {
                item(
                    item.id,
                    "It's an icon from an old ring. With the help of a Fremennik, " +
                            "you might be able to combine it with something else."
                )
            }
        }

    }

    override fun getItems(): IntArray {
        return intArrayOf(ARCHER_ICON, BERSERKER_ICON, SEERS_ICON, WARRIOR_ICON)
    }
}