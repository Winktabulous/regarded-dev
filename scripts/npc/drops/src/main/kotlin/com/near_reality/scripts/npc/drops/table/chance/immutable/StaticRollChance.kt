package com.near_reality.scripts.npc.drops.table.chance.immutable

import com.near_reality.scripts.npc.drops.table.DropTable
import com.near_reality.scripts.npc.drops.table.chance.RollChance

/**
 * Represents a roll-able chance in a [DropTable].
 *
 * @author Stan van der Bend
 */
interface StaticRollChance : RollChance {

    /**
     * the odds of this chance being rolled.
     */
    val rarity: Int
}
