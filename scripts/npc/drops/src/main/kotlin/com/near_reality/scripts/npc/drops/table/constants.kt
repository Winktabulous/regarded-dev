package com.near_reality.scripts.npc.drops.table

import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollNothingChance


/**
 * Represents a [StaticRollChance.rarity] that always rolls.
 */
const val always = Int.MAX_VALUE

const val never = Int.MIN_VALUE

/**
 * Represents a [StaticRollNothingChance].
 */
val nothing = StaticRollNothingChance()

/**
 * Creates a noted [DropQuantity].
 */
val Int.noted: DropQuantity
    get() = DropQuantity(this..this, true)

val IntRange.noted: DropQuantity
    get() = DropQuantity(this, true)
