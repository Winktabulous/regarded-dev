package com.near_reality.api.util

import com.near_reality.api.model.Skill
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

/**
 * Represents a lookup table, where the index is the skill level minus 1,
 * and the value is the estimated experience at that level.
 *
 * **Formula:** [OSRS Wiki](https://oldschool.runescape.wiki/w/Experience#Formula)
 */
private val levelExperienceLookup = DoubleArray(99) {
    val level = (it + 1).toDouble()
    val gamma = 0.5772156649015329
    val experienceEstimate = (level.pow(2) / 8.0) - ((9.0 / 40.0) * level) + (75.0 * ((2.0.pow(level / 7.0) - 2.0.pow(1.0 / 7.0)) / (2.0.pow(1.0 / 7.0) - 1.0))) - gamma
    experienceEstimate.coerceAtLeast(0.0)
}

/**
 * Returns the minimum experience for this [level][Int].
 *
 * @see levelExperienceLookup
 */
fun Int.toExperience() = levelExperienceLookup[this - 1].toInt()

/**
 * Returns the minimum level for this [experience][Double].
 *
 * @see levelExperienceLookup
 */
fun Number.toLevel() : Int  {
    val xp = this.toInt()
    // closest level to this double
    val lvl = levelExperienceLookup.withIndex().minByOrNull { abs(it.value - xp) }
    return if(lvl == null) {
        99
    } else {
        lvl.index+1
    }
}

/**
 * Calculates the [combat level][Int] for the argued [skillLevels].
 */
fun calculateCombatLevel(skillLevels: Map<Skill, Int>): Int {
    val attack: Int = skillLevels[Skill.ATTACK] ?: 1
    val defence: Int = skillLevels[Skill.DEFENCE] ?: 1
    val strength: Int = skillLevels[Skill.STRENGTH] ?: 1
    val hp: Int = skillLevels[Skill.HITPOINTS] ?: 10
    val prayer: Int = skillLevels[Skill.PRAYER] ?: 10
    val ranged: Int = skillLevels[Skill.RANGED] ?: 1
    val magic: Int = skillLevels[Skill.MAGIC] ?: 1
    var baseLevel = (defence + hp + floor((prayer / 2.0f).toDouble())) * 0.25f
    val melee = (attack + strength) * 0.325
    val ranger = floor(ranged * 1.5) * 0.325
    val mage = floor(magic * 1.5) * 0.325
    baseLevel += when {
        melee >= ranger && melee >= mage -> melee
        ranger >= melee && ranger >= mage -> ranger
        else -> mage
    }
    return 126.0.coerceAtMost(baseLevel).toInt()
}
