package com.near_reality.plugins.item

import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Handles removal of basilik jaw from neitiznot faceguard.
 *
 * @author Stan van der Bend
 *
 * @param player the [Player] who is dismantling their faceguard.
 */
class NeitiznotFaceguardDismantleDialogue(player: Player) : Dialogue(player) {
    override fun buildDialogue() {
        options("Remove the jaw from the helmet?") {
            "Yes." {
                val inventory = player.inventory
                if (inventory.deleteItem(ItemId.NEITIZNOT_FACEGUARD, 1).result == RequestResult.SUCCESS) {
                    inventory.addOrDrop(ItemId.HELM_OF_NEITIZNOT)
                    inventory.addOrDrop(ItemId.BASILISK_JAW)
                    player.dialogue {
                        doubleItem(
                            ItemId.HELM_OF_NEITIZNOT,
                            ItemId.BASILISK_JAW,
                            "You remove the Basilisk Jaw from the Neitiznot Faceguard."
                        )
                    }
                }
            }
            "No." {}
        }
    }
}
