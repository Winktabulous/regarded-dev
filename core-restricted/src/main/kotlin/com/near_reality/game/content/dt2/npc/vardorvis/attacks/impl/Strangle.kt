package com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl

import com.near_reality.game.content.dt2.npc.vardorvis.attacks.Attack

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-08
 */
object Strangle : Attack {
    override fun getRequiredHp(): Double = 80.00
    override fun getFirstRate() = 2
    override fun getSecondRate() = 4
    override fun getEnrageRate() = 10
    override fun getEnabled(): Boolean = true
    override fun getAutoHeal(): Boolean = false
    override fun requiredCooldownAttacks(enrage: Boolean): Int = 3
}