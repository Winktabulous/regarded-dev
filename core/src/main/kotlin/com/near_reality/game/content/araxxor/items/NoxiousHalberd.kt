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
class NoxiousHalberd : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        val blade = player?.inventory?.getItemById(NOXIOUS_BLADE)
        val point = player?.inventory?.getItemById(NOXIOUS_POINT)
        val pommel = player?.inventory?.getItemById(NOXIOUS_POMMEL)
        if (blade == null || point == null || pommel == null) {
            player?.sendMessage("You are missing pieces to this halberd.")
            return
        }
        player.dialogue {
            plain("Do you wish to combine all three pieces to create a noxious halberd? This process is non-reversible.")
            options("Do you wish to create a noxious halberd?",
                Dialogue.DialogueOption("Yes.") {
                    if (player.inventory?.deleteItems(blade, pommel, point)?.result == RequestResult.SUCCESS) {
                        player.inventory?.addItem(Item(NOXIOUS_HALBERD))
                        player.dialogue { item(NOXIOUS_HALBERD, "You successfully create a noxious halberd.") }
                    }
                },
                Dialogue.DialogueOption("No.") { player.interfaceHandler.closeInterfaces() }
            )
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(NOXIOUS_POINT, NOXIOUS_BLADE, NOXIOUS_POMMEL)
}