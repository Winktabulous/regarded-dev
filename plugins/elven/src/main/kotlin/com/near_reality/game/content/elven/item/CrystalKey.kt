package com.near_reality.game.content.elven.item

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ItemOnItemAction.ItemPair
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.world.entity.player.Player

/**
 * @author Kris | 04/04/2019 12:49
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
@Suppress("UNUSED")
class CrystalKey : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val inventory = player.inventory
        inventory.deleteItemsIfContains(arrayOf(from, to)) {
            inventory.addOrDrop(Item(989))
            player.sendMessage("You join the two halves of the key together.")
        }
    }

    override fun getMatchingPairs() =
        arrayOf(ItemPair.of(985, 987))
}
