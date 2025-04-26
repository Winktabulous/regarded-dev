package com.near_reality.game.content.araxxor.items

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class RancourAmulet : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        val fang = player?.inventory?.getItemById(ARAXYTE_FANG)
        val amulet = player?.inventory?.getItemById(AMULET_OF_TORTURE)
        if (fang == null || amulet == null) {
            player?.sendMessage("You are missing pieces to this amulet.")
            return
        }
        player.dialogue {
            plain("Do you wish to use the araxyte fang on your amulet of torture? This process is non-reversible and will consume both items.")
            options("Do you wish to create a amulet of rancour?",
                Dialogue.DialogueOption("Yes.") {
                    if (player.inventory?.deleteItems(fang, amulet)?.result == RequestResult.SUCCESS) {
                        player.inventory?.addItem(Item(AMULET_OF_RANCOUR))
                        player.dialogue { item(AMULET_OF_RANCOUR, "You successfully create a amulet of rancour.") }
                    }
                },
                Dialogue.DialogueOption("No.") { player.interfaceHandler.closeInterfaces() }
            )
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(AMULET_OF_TORTURE, ARAXYTE_FANG)
}