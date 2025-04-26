package com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe

import com.near_reality.game.content.dt2.npc.vardorvis.attacks.Attack

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-08
 */
data object SwingingAxes : Attack {
    override fun getRequiredHp(): Double = 100.00
    override fun getFirstRate() = 20
    override fun getSecondRate() = 50
    override fun getEnrageRate() = 100
    override fun getEnabled(): Boolean = true
    override fun getAutoHeal(): Boolean = false
    override fun requiredCooldownAttacks(enrage: Boolean): Int = if (enrage) 3 else 4
}
