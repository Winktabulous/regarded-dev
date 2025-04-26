package com.near_reality.game.content.dt2.npc.vardorvis.attacks

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-08
 */
interface Attack {
    /* Required HP remaining as % for attack to be done */
    fun getRequiredHp(): Double

    /* Odds out of 100 to roll this special attack at 100% HP */
    fun getFirstRate(): Int

    /* Odds out of 100 to roll this special attack at 66% HP */
    fun getSecondRate(): Int

    /* Odds out of 100 to roll this special attack at 33% HP */
    fun getEnrageRate(): Int

    /* Auto-heal enabled for 50% of damage dealt from this Attack */
    fun getAutoHeal(): Boolean

    /* Enabled to use or not */
    fun getEnabled(): Boolean

    /* Amount of attacks required before running again */
    fun requiredCooldownAttacks(enrage: Boolean): Int = 0
}