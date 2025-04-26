package com.near_reality.scripts.npc.drops.table.chance.immutable

import com.near_reality.scripts.npc.drops.table.DropQuantity
import com.near_reality.scripts.npc.drops.table.chance.RollItemChance
import com.zenyte.game.item.Item

/**
 * Represents a [StaticRollChance] for [items][Item].
 *
 * @param name      the [name][String] of the Item.
 * @param quantity  the [DropQuantity] that defines the drop range or static amount to be dropped.
 * @param rarity    the [StaticRollChance.rarity].
 *
 * @author Stan van der Bend
 */
open class StaticRollItemChance(
    id: Int,
    quantity: DropQuantity,
    override val rarity: Int = 1
) : RollItemChance(id, quantity), StaticRollChance
