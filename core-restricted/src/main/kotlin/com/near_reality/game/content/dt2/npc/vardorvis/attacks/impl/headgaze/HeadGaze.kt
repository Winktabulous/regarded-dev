package com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.headgaze

import com.near_reality.game.content.dt2.npc.vardorvis.attacks.Attack

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-08
 */
object HeadGaze : Attack {
    override fun getRequiredHp(): Double = 100.00
    override fun getFirstRate() = 10
    override fun getSecondRate() = 15
    override fun getEnrageRate() = 45
    override fun getEnabled(): Boolean = true
    override fun getAutoHeal(): Boolean = false
    override fun requiredCooldownAttacks(enrage: Boolean): Int = if (enrage) 3 else 7
}