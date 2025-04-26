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
class WebWeaverBowCreationPlugin : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        if (player.skills.getLevel(SkillConstants.FLETCHING) < 85) {
            player.sendMessage("You need 85 Fletching in order to combine these items.")
            return
        }

        val craws = if (from.id == ItemId.FANGS_OF_VENENATIS) to else from
        player.inventory.deleteItem(Item(ItemId.FANGS_OF_VENENATIS))
        player.inventory.deleteItem(craws)

        val webweaver = Item(if (craws.id == ItemId.CRAWS_BOW_U) ItemId.WEBWEAVER_BOW_U_27652 else ItemId.WEBWEAVER_BOW_27655)
        webweaver.charges = craws.charges
        player.inventory.addItem(webweaver)

        player.dialogue {
            item(webweaver, "You combine the Craw's bow and the Fangs of Venenatis together to form the Webweaver bow.")
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(ItemId.CRAWS_BOW_U, ItemId.FANGS_OF_VENENATIS),
            ItemOnItemAction.ItemPair.of(ItemId.CRAWS_BOW, ItemId.FANGS_OF_VENENATIS)
        )
    }
}
