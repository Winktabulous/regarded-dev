package com.near_reality.game.content.boss.nex.`object`.actions

import com.near_reality.game.content.boss.nex.item.BandosOnAncientForge
import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectId

class AncientForgeObjectAction : ObjectActionScript() {
    init {
        ObjectId.ANCIENT_FORGE_42966 {
            val possible = listOfNotNull(
                player.inventory.getAny(ItemId.BANDOS_CHESTPLATE),
                player.inventory.getAny(ItemId.BANDOS_TASSETS),
            )
            if (possible.isEmpty()) {
                player.dialogue { plain("You don't have any bandos items to melt.") }
                return@ANCIENT_FORGE_42966
            }
            BandosOnAncientForge().handleItemOnObjectAction(player, possible.first(), -1, obj)
        }
    }
}


