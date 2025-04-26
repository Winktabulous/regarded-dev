package com.near_reality.scripts.npc.drops.table

import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollItemChance

/**
 * Defines how much of an item is dropped for [item drop rolls][StaticRollItemChance].
 *
 * @param range the range of values to pick from upon [StaticRollItemChance.rollItem].
 * @param noted `true` if a noted variant should be dropped, `false` if not.
 *
 * @author Stan van der Bend
 */
data class DropQuantity(val range: IntRange, internal val noted: Boolean = false) {

    init {
        require(range.first > 0 && range.last > 0) {
            "DropQuantity.range `first` and `last` must be > 0"
        }
        require(range.last >= range.first) {
            "DropQuantity.range `last` must be larger than or equal to `first`"
        }
    }

    /**
     * Returns the [IntRange.first] element if [IntRange.first] equals [IntRange.last].
     * Otherwise, returns a random element from [range] using [IntRange.random].
     */
    fun rollQuantity()  = if (range.first == range.last)
        range.first
    else
        range.random()

    override fun toString() = if (range.first == range.last)
        "${range.first} x"
    else
        "${range.first}..${range.last} x"
}
