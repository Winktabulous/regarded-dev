package com.near_reality.scripts.npc.drops.table.dsl

import com.near_reality.scripts.npc.drops.table.DropQuantity
import com.near_reality.scripts.npc.drops.table.DropTable
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.chance.*
import com.near_reality.scripts.npc.drops.table.chance.dynamic.DynamicRollChance
import com.near_reality.scripts.npc.drops.table.chance.dynamic.DynamicRollItemChance
import com.near_reality.scripts.npc.drops.table.chance.dynamic.DynamicRollItemOneIn
import com.near_reality.scripts.npc.drops.table.chance.immutable.*
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.enums.RareDrop
import com.zenyte.game.world.entity.player.Player

/**
 * Represents a DSL builder for [drop rolls][StaticRollChance] and stored them in [staticRolls].
 *
 * @author Stan van der Bend
 */
open class DropTableBuilder {

    /**
     * A list of [StaticRollChance] objects.
     */
    val staticRolls = mutableListOf<StaticRollChance>()

    val dynamicRolls = mutableListOf<DynamicRollChance>()

    val everywhere = Any()

    /**
     * Creates a new [PartialRollTableChance] with a [StaticRollChance.rarity] equal to [rarity].
     */
    infix fun chance(rarity: Int) = PartialRollTableChance(rarity)

    infix fun <T : RollItemChance> T.announce(@Suppress("UNUSED_PARAMETER") any: Any): T {
        RareDrop.addStatic(id)
        return this
    }

    /**
     * Adds a new [StaticRollTableChance] that rolls from the [dropTable]
     * with a rarity equal to this [PartialRollTableChance.rarity].
     */
    infix fun PartialRollTableChance.roll(dropTable: StandaloneDropTableBuilder) {
        staticRolls += StaticRollTableChance(rarity, dropTable)
    }

    /**
     * Adds a new [StaticRollItemChance] of an item with as name [PartialRollItemChance.name],
     * an amount rolled from [PartialRollItemChance.quantity],
     * a rarity equal to this [PartialRollTableChance.rarity].
     */
    infix fun PartialRollItemChance.rarity(rarity: Int) =
        (if (rarity == always)
            StaticRollItemAlways(id, quantity)
        else
            StaticRollItemChance(id, quantity, rarity)
        ).also(staticRolls::add)


    infix fun PartialRollItemChance.dynamicRarity(dynamic: Player.() -> Int) =
        DynamicRollItemChance(id, quantity, dynamic)
            .also(dynamicRolls::add)

    infix fun PartialRollItemChance.oneIn(rarity: Int) =
        StaticRollItemOneIn(id, quantity, rarity).also(staticRolls::add)

    infix fun PartialRollItemChance.dynamicOneIn(dynamic: Player.() -> Int) =
        DynamicRollItemOneIn(id, quantity, dynamic).also(dynamicRolls::add)

    /**
     * Adds a new [StaticRollNothingChance] with a rarity equal to [rarity].
     */
    infix fun StaticRollNothingChance.rarity(rarity: Int) =
        StaticRollNothingChance(rarity).also(staticRolls::add)

    /**
     * Creates a new [PartialRollItemChance] from this [name][String]
     * with a [PartialRollItemChance.quantity] equal to [quantity].
     */
    infix fun Int.quantity(quantity: DropQuantity) =
        PartialRollItemChance(this, quantity)

    /**
     * Creates a new [PartialRollItemChance] from this [name][String]
     * with a [PartialRollItemChance.quantity] equal to [quantity].
     */
    infix fun Int.quantity(quantity: Int) =
        PartialRollItemChance(this, DropQuantity(quantity..quantity))

    /**
     * Creates a new [PartialRollItemChance] from this [name][String]
     * with a [PartialRollItemChance.quantity] equal to [quantity].
     */
    infix fun Int.quantity(quantity: IntRange) =
        PartialRollItemChance(this, DropQuantity(quantity))

    infix fun RollItemChance.onlyDroppedBy(npcId: Int) =
        also { addNpcIds(npcId) }

    fun RollItemChance.onlyDroppedBy(vararg npcsIds: Int) =
        also { addNpcIds(*npcsIds) }

    infix fun RollItemChance.transformItem(dynamic: Player.(Item) -> Item?) =
        also { transformer = dynamic }

    infix fun RollItemChance.info(stringProvider: () -> String) =
        also { viewerInfo = stringProvider() }

    /**
     * Builds a new [DropTable].
     */
    fun build(limit: Int) = DropTable(limit, staticRolls, dynamicRolls)
}

