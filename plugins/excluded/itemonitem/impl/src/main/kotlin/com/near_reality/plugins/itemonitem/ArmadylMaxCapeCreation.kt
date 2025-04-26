package com.near_reality.plugins.itemonitem

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

@Suppress("unused")
class ArmadylMaxCapeCreation : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        player.dialogue {
            options("Do you want to combine your max cape with your god cape?") {
                "Yes." {
                    val inventory = player.inventory
                    if (inventory.deleteItems(from, to).result == RequestResult.SUCCESS)
                        inventory.addItem(CustomItemId.ARMADYL_MAX_CAPE, 1)
                }
                "No." {}
            }
        }
    }

    override fun getItems(): IntArray
        = intArrayOf(ItemId.IMBUED_ARMADYL_CAPE, ItemId.MAX_CAPE_13342)
}