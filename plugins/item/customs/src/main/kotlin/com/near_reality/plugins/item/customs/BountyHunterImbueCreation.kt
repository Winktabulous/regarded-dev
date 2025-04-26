package com.near_reality.plugins.item.customs

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class BountyHunterImbueCreation : PairedItemOnItemPlugin {
    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val target = if(from.isTargetItem()) from else to
        player.dialogue {
            plain("This is an irreversible process and you will not be able to get your scroll back.")
            options("Are you sure you want to imbue your weapon?") {
                "Yes, I want more power." {
                    player.inventory.run {
                        if(deleteItems(from, to).result == RequestResult.SUCCESS) {
                            player.sendMessage("You have created the unique variant of your item!")
                            addItem(Item(target.converted()))
                        }
                    }
                }
                "No, I want to keep my items" {}
            }
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(ItemId.DARK_BOW, ItemId.DARK_BOW_IMBUE_SCROLL),
            ItemOnItemAction.ItemPair.of(ItemId.BARRELCHEST_ANCHOR, ItemId.BARRELCHEST_ANCHOR_IMBUE_SCROLL),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_MACE, ItemId.DRAGON_MACE_IMBUE_SCROLL),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_LONGSWORD, ItemId.DRAGON_LONGSWORD_IMBUE_SCROLL),
            ItemOnItemAction.ItemPair.of(ItemId.ABYSSAL_DAGGER, ItemId.ABYSSAL_DAGGER_IMBUE_SCROLL),
        )
    }

    private fun Item.converted() : Int {
        when(this.id) {
            ItemId.DARK_BOW -> return ItemId.DARK_BOW_BH
            ItemId.BARRELCHEST_ANCHOR -> return ItemId.BARRELCHEST_ANCHOR_BH
            ItemId.DRAGON_MACE -> return ItemId.DRAGON_MACE_BH
            ItemId.DRAGON_LONGSWORD -> return ItemId.DRAGON_LONGSWORD_BH
            ItemId.ABYSSAL_DAGGER -> return ItemId.ABYSSAL_DAGGER_BH
        }
        return 0
    }


    private fun Item.isTargetItem() : Boolean {
        when(this.id) {
            ItemId.DARK_BOW,
            ItemId.BARRELCHEST_ANCHOR,
            ItemId.DRAGON_MACE,
            ItemId.DRAGON_LONGSWORD,
            ItemId.ABYSSAL_DAGGER -> return true
        }
        return false
    }
}