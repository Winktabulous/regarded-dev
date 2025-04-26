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
class UrsineChainMaceCreationPlugin : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        if (player.skills.getLevel(SkillConstants.SMITHING) < 85) {
            player.sendMessage("You need 85 Smithing in order to combine these items.")
            return
        }

        val viggoras = if (from.id == ItemId.CLAWS_OF_CALLISTO) to else from
        player.inventory.deleteItem(Item(ItemId.CLAWS_OF_CALLISTO))
        player.inventory.deleteItem(viggoras)

        val ursine =
            Item(if (viggoras.id == ItemId.VIGGORAS_CHAINMACE_U) ItemId.URSINE_CHAINMACE_U_27657 else ItemId.URSINE_CHAINMACE_27660)
        ursine.charges = viggoras.charges
        player.inventory.addItem(ursine)

        player.dialogue {
            item(ursine, "You combine the Viggora's chainmace and the Claws of Callisto together to form the Ursine chainmace.")
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(ItemId.VIGGORAS_CHAINMACE_U, ItemId.CLAWS_OF_CALLISTO),
            ItemOnItemAction.ItemPair.of(ItemId.VIGGORAS_CHAINMACE, ItemId.CLAWS_OF_CALLISTO)
        )
    }
}
