package com.near_reality.game.content.dt2.plugins.rings

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

@Suppress("unused")
class CatalystOnMagusRingAction : ItemOnItemAction {

    private val catalyst = Item(ItemId.MAGUS_VESTIGE, 1)
    private val tablets = Item(ItemId.FROZEN_TABLET, 15)
    private val ring = Item(ItemId.MAGUS_RING_28313, 1)

    private val product = Item(ItemId.MAGUS_RING_A, 1)

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        player.dialogue {
            item(ItemId.MAGUS_RING_A, "The Magus ring can be upgraded to an awakened variant with 15 frozen tablets and 1 Magus catalyst as well as Level 95 in the Magic skill to add both additional attack boosts and defense boosts.")
            if(player.hasRequiredItems() && player.hasRequiredSkills()) {
                options("Would you like to create the awakened version of the ring?") {
                    "Yes" {
                        if (player.inventory.deleteItems(catalyst, tablets, ring).result == RequestResult.SUCCESS) {
                            player.inventory.addItem(product)
                        }
                    }
                    "No" { finish() }
                }
            } else {
                plain("You currently lack the required materials or skill.")
            }
        }
    }

    override fun getItems(): IntArray? = null

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair(ItemId.MAGUS_VESTIGE, ItemId.MAGUS_RING_28313),
            ItemOnItemAction.ItemPair(ItemId.MAGUS_RING_28313, ItemId.FROZEN_TABLET)
        )
    }

    private fun Player.hasRequiredItems() : Boolean = inventory.containsItems(catalyst, tablets, ring)
    private fun Player.hasRequiredSkills() : Boolean = skills.getLevel(Skills.MAGIC) >= 95

}
