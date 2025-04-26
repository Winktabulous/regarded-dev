package com.near_reality.game.content.dt2.npc.whisperer

import com.near_reality.game.content.dt2.npc.whisperer.WhispyUtils.rand
import com.near_reality.game.content.dt2.npc.whisperer.attacks.basic.WhispererBasicAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.basic.impl.MagicAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.basic.impl.RangedAttack

sealed interface WhispererPhase {

    val specialHpPercentThreshold: Float

    fun generateAttacks(): Iterable<WhispererBasicAttack>

    data object First : WhispererPhase {
        override val specialHpPercentThreshold: Float = 0.8F
        override fun generateAttacks(): Iterable<WhispererBasicAttack> {
            val isRanged = rand(1) == RANGED_ATTACK_ID
            val projectile = if (isRanged) RangedAttack() else MagicAttack()
            return listOf(projectile, projectile, projectile)
        }
    }

    data object Second : WhispererPhase {
        override val specialHpPercentThreshold: Float = 0.55F
        override fun generateAttacks(): Iterable<WhispererBasicAttack> {
            val isRanged = rand(1) == RANGED_ATTACK_ID
            val leadingAttack = if (isRanged) RangedAttack() else MagicAttack()
            val trailingAttack = if (isRanged) MagicAttack() else RangedAttack()
            return listOf(leadingAttack, leadingAttack, trailingAttack)
        }
    }

    data object Third : WhispererPhase {
        override val specialHpPercentThreshold: Float = 0.3F
        override fun generateAttacks(): Iterable<WhispererBasicAttack> {
            val isRanged = rand(1) == RANGED_ATTACK_ID
            val leadingAttack = if (isRanged) RangedAttack() else MagicAttack()
            val trailingAttack = if (isRanged) MagicAttack() else RangedAttack()
            return listOf(leadingAttack, trailingAttack, leadingAttack)
        }
    }

    data object Fourth : WhispererPhase {
        override val specialHpPercentThreshold: Float = 0.0F
        override fun generateAttacks(): Iterable<WhispererBasicAttack> {
            val isRanged = rand(1) == RANGED_ATTACK_ID
            val leadingAttack = if (isRanged) RangedAttack() else MagicAttack()
            val trailingAttack = if (isRanged) MagicAttack() else RangedAttack()
            return listOf(leadingAttack, trailingAttack, leadingAttack)
        }
    }

    companion object {
        const val RANGED_ATTACK_ID = 0
    }
}