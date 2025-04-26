package com.near_reality.scripts.npc.drops.table.dsl

import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollTableChance

/**
 * Represents a temporary state only used by the DSL for building a [StaticRollTableChance].
 *
 * @param rarity the rarity to set [StaticRollTableChance.rarity] as.
 *
 * @author Stan van der Bend
 */
class PartialRollTableChance(internal val rarity: Int)
