package com.near_reality.game.content.imbue

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.enums.ImbueableItem
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

object DisimbueItemHandler {
    fun disimbueItem(player: Player, item: Item) {
        if(item.id == ItemId.RING_OF_SUFFERING_RI || item.id == ItemId.RING_OF_SUFFERING)
            return
        val imbueable = ImbueableItem.get(item.id) ?: return
        player.dialogue {
            options("Remove the Imbued bonus from this item?") {
                dialogueOption("Yes - you will be refunded 1x Scroll of imbuing", noPlayerMessage = true) {
                    val inventory = player.inventory
                    if (inventory.hasSpaceFor(ItemId.SCROLL_OF_IMBUING)) {
                        if (inventory.containsAnyOf(item.id)) {
                            inventory.deleteItem(item.id, 1)
                            inventory.addOrDrop(imbueable.normal, 1)
                            inventory.addOrDrop(ItemId.SCROLL_OF_IMBUING)
                        }
                    } else {
                        player.sendMessage("You do not have enough inventory space to receive the Scroll of imbuing.")
                    }
                }
                dialogueOption("No - keep the imbued item.",  noPlayerMessage = true) {

                }
            }
        }
    }
}
