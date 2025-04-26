package com.near_reality.plugins.item.customs

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Handles the creation of an Ancient medallion and Ancient book.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class AncientItemCreation : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val eyeItem = when {
            from.id == CustomItemId.ANCIENT_EYE -> from
            to.id == CustomItemId.ANCIENT_EYE -> to
            else -> error("Did not find ancient eye (from=$from, to=$to)")
        }
        val other = if (from == eyeItem) to else from
        val result = when(other.id) {
            ItemId.MAGES_BOOK -> Item(CustomItemId.ANCIENT_BOOK_32004)
            ItemId.OCCULT_NECKLACE -> Item(CustomItemId.ANCIENT_MEDALLION_32024)
            else -> error("Cannot use $eyeItem on $other")
        }
        player.dialogue {
            doubleItem(eyeItem, other, "Are you sure you with to combine your ${eyeItem.name} and ${other.name} into a ${result.name}?")
            options("Combine your ${eyeItem.name} and ${other.name} into a ${result.name}?") {
                "Yes." {
                    if (player.inventory.deleteItems(eyeItem, other).result == RequestResult.SUCCESS) {
                        player.inventory.addItem(result)
                        player.dialogue {
                            item(result, "You combine your ${eyeItem.name} and ${other.name} into a ${result.name}")
                        }
                    }
                }
                "No." {}
            }
        }
    }

    override fun getMatchingPairs() = arrayOf(
        ItemOnItemAction.ItemPair(CustomItemId.ANCIENT_EYE, ItemId.MAGES_BOOK),
        ItemOnItemAction.ItemPair(CustomItemId.ANCIENT_EYE, ItemId.OCCULT_NECKLACE)
    )
}
