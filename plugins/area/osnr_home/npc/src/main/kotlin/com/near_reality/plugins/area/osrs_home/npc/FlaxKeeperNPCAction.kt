package com.near_reality.plugins.area.osrs_home.npc

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player

class FlaxKeeperNPCAction : NPCActionScript() {

    init {
        npcs(NpcId.FLAX_KEEPER)

        "Exchange" {
            player.exchangeFlax()
        }
    }

    private fun Player.exchangeFlax() {
        val flaxInInventory = inventory.getAmountOf(ItemId.FLAX)
        if(flaxInInventory == 0) {
            sendFilteredMessage("You don't have any flax to exchange.")
            return
        }
        inventory.deleteItem(ItemId.FLAX, flaxInInventory)
        inventory.addItem(Item(ItemId.FLAX + 1, flaxInInventory))
        sendFilteredMessage("He grunts and exchanges your flax for some bank notes")
    }

}