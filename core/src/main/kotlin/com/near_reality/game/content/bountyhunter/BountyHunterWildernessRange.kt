package com.near_reality.game.content.bountyhunter

import java.util.*

/**
 * Represents a wilderness range that a player is currently in. The [display] field is
 * what shows on the BountyHunter Interface under Component [BountyHunterVars.I_CHILD_TARGET_WILDERNESS_LVL]
 * @author John J. Woloszyk / Kryeus
 */
enum class BountyHunterWildernessRange (val minimum: Int, val max: Int, val display: String) {

    T1(1, 4, "1-4"),
    T2(5,9, "5-9"),
    T3(10, 14, "10-14"),
    T4(15, 19, "15-19"),
    T5(20, 24, "20-24"),
    T6(25, 29, "25-29"),
    T7(30, 34, "30-34"),
    T8(35, 39, "35-39"),
    T9(40, 44, "40-44"),
    T10(45, 49, "45-49"),
    T11(50, 56, "50-56"),
    UNKNOWN(99, 99, "---");



    companion object {
        private val RANGES: Set<BountyHunterWildernessRange> = Collections.unmodifiableSet(EnumSet.allOf(BountyHunterWildernessRange::class.java))

        @JvmStatic fun getForWildernessLevel(wildylvl: Int) : BountyHunterWildernessRange {
            return RANGES.stream().filter{wildylvl >= it.minimum && wildylvl <= it.max }.findAny().orElse(T1)
        }
    }
}