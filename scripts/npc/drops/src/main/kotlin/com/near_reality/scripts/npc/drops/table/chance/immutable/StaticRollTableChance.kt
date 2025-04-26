package com.near_reality.scripts.npc.drops.table.chance.immutable

import com.near_reality.scripts.npc.drops.table.chance.RollTableChance
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder

/**
 * Represents a [StaticRollChance] for [standalone drop tables][StandaloneDropTableBuilder].
 *
 * @param rarity    the [StaticRollChance.rarity].
 * @param dropTable the nested [StandaloneDropTableBuilder] to roll from.
 *
 * @author Stan van der Bend
 */
class StaticRollTableChance(override val rarity: Int, override val dropTable: StandaloneDropTableBuilder)
    : StaticRollChance, RollTableChance
