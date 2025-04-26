package com.near_reality.game.content.elven.item

import com.near_reality.game.content.crystal.CRYSTAL_SHARD
import com.near_reality.game.content.crystal.CRYSTAL_SHARD_CHARGES_RATIO
import com.near_reality.game.content.crystal.recipes.CrystalChargeable
import com.near_reality.game.content.elven.produce
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction.ItemPair
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Handles the charging of crystal items.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class ChargeCrystalItem : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val (crystalItem, slot) = if (from.id == ItemId.CRYSTAL_SHARD) to to toSlot else from to fromSlot

        val crystalWearable = CrystalChargeable.all.find {
            it.productItemId == crystalItem.id || it.inactiveId == crystalItem.id
        }!!
        val inactive = crystalItem.id == crystalWearable.inactiveId

        if(inactive) {
            player.inventory.set(slot, crystalWearable.produce())
            return
        }
    }

    override fun getMatchingPairs() = CrystalChargeable.all
        .flatMap {
            listOf(
                ItemPair.of(it.productItemId, CRYSTAL_SHARD),
                ItemPair.of(it.inactiveId, CRYSTAL_SHARD)
            )
        }
        .toTypedArray()
}
