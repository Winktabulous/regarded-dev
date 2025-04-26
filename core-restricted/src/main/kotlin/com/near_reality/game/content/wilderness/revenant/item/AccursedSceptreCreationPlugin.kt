package com.near_reality.game.content.wilderness.revenant.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Andys1814
 */
@Suppress("unused")
class AccursedSceptreCreationPlugin : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        if (player.skills.getLevel(SkillConstants.CRAFTING) < 85) {
            player.sendMessage("You need 85 Crafting in order to combine these items.")
            return
        }

        val thammarons = if (from.id == ItemId.SKULL_OF_VETION) to else from
        player.inventory.deleteItem(Item(ItemId.SKULL_OF_VETION))
        player.inventory.deleteItem(thammarons)

        val accursed = Item(
            when (thammarons.id) {
                ItemId.THAMMARONS_SCEPTRE_U -> ItemId.ACCURSED_SCEPTRE_U_27662
                ItemId.THAMMARONS_SCEPTRE_AU -> ItemId.ACCURSED_SCEPTRE_AU_27676
                ItemId.THAMMARONS_SCEPTRE -> ItemId.ACCURSED_SCEPTRE_27665
                ItemId.THAMMARONS_SCEPTRE_A -> ItemId.ACCURSED_SCEPTRE_A_27679
                else -> {
                    player.sendDeveloperMessage("Invalid item id: ${thammarons.id}")
                    return
                }
            }
        )
        accursed.charges = thammarons.charges
        player.inventory.addItem(accursed)

        player.dialogue {
            item(
                accursed,
                "You combine the Thammaron's sceptre and the Skull of Vet'ion together to form the Accursed Sceptre."
            )
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(ItemId.THAMMARONS_SCEPTRE_U, ItemId.SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(ItemId.THAMMARONS_SCEPTRE_AU, ItemId.SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(ItemId.THAMMARONS_SCEPTRE, ItemId.SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(ItemId.THAMMARONS_SCEPTRE_A, ItemId.SKULL_OF_VETION)
        )
    }
}
