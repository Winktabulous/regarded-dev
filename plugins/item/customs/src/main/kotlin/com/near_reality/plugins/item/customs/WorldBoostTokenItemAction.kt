package com.near_reality.plugins.item.customs

import com.near_reality.game.plugin.optionsMenu
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.content.serverevent.WorldBoostType
import com.zenyte.game.content.xamphur.XamphurBoost
import com.zenyte.game.content.xamphur.XamphurHandler
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.options

class WorldBoostTokenItemAction : ItemActionScript() {
    init {
        items(ItemId.WORLD_BOOST_TOKEN)

        "Rub" {
            player.optionsMenu(
                options = XamphurBoost.entries,
                title = "Choose a boost to activate or extend.",
                stringifier = { boost: WorldBoostType ->
                    buildString {
                        append(if (World.getWorldBoosts().any { boost == it.boostType }) "Extend" else "Activate")
                        append(' ')
                        append(boost.mssg)
                    }
                }
            ) { boost ->
                val prefix = if (World.getWorldBoosts().any { boost == it.boostType }) "extend" else "activate"
                player.options("Are you sure?") {
                    dialogueOption("Yes, $prefix ${boost.mssg}", true) {
                        val itemAtSlot = container.get(slotID)
                        if (itemAtSlot.id == ItemId.WORLD_BOOST_TOKEN
                            && container.remove(slotID, 1).result == RequestResult.SUCCESS
                        ) {
                            container.isFullUpdate = true
                            container.refresh(slotID)
                            container.refresh(player)
                            XamphurHandler.activateBoost(player, boost, 1)
                        }
                    }
                    "No, I changed my mind."()
                }
            }
        }
    }
}

