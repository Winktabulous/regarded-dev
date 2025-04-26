package com.near_reality.game.world.entity

import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.areatype.AreaTypes
import java.lang.ref.WeakReference
import java.util.*

private val combatControllers = WeakHashMap<AbstractEntity, CombatController>()

val AbstractEntity.combatController get() = combatControllers.getOrPut(this) { CombatController(this) }

class CombatController(val entity: AbstractEntity) {

    private var lastAttackInitiated: AttackSnapshot? = null
    private var lastAttackedBy: AttackedSnapshot? = null

    private var activeTarget: AbstractEntity? = null

    fun markTarget(target: AbstractEntity) {
        activeTarget = target
    }

    fun markAttack(target: AbstractEntity) {
        lastAttackInitiated = AttackSnapshot(target)
    }

    fun clearTarget() {
        activeTarget = null
    }

    fun isTargeting(target: AbstractEntity): Boolean {
        return activeTarget == target
    }

    fun lastAttackInitiatedSince(duration: Int): AttackSnapshot? =
        lastAttackInitiated?.takeIf { it.timeSince() <= duration }

    fun lastAttackedBySince(duration: Int): AttackedSnapshot? =
        lastAttackedBy?.takeIf { it.timeSince() <= duration }

    fun canAttack(target: AbstractEntity) {
        val policies = mutableSetOf<OutgoingAttackFlag>()
        val attacker = entity
        if (target is Player) {
            val targetInSinglesPlus = AreaTypes.SINGLES_PLUS.matches(target)
            val targetStealCooldown = if(targetInSinglesPlus) 20 else 8
            val targetCombat = target.combatController
            /*
             * If the target has been attacked within the last steal cooldown cycles,
             */
            val targetLastAttackedBy = targetCombat.lastAttackedBySince(targetStealCooldown)
            if (targetLastAttackedBy != null) {
                if (targetInSinglesPlus) {
                    if (targetLastAttackedBy.attacker.get() is Player) {

                    }
                }
            }
        }
    }

    fun getAttackFlags() {
        val flags = mutableSetOf<OutgoingAttackFlag>()
        val lastAttackInitiated = lastAttackInitiated?.takeUnless { it.cooledDown() }
        if (lastAttackInitiated != null) {
            flags += OutgoingAttackFlag.RecentlyInitiatedAttacked
        }
    }
}

private fun getLocalStealCooldown(target: AbstractEntity): Int {
    return if(AreaTypes.SINGLES_PLUS.matches(target)) 20 else 8
}

class AttackSnapshot(
    target: AbstractEntity
) {
    val time = WorldThread.getCurrentCycle()
    val target = WeakReference(target)
    fun timeSince() = WorldThread.getCurrentCycle() - time
    fun cooledDown() = target.get()?.let { timeSince() >= getLocalStealCooldown(it) }?: true
}

class AttackedSnapshot(
    attacker: AbstractEntity
) {
    val time = WorldThread.getCurrentCycle()
    val attacker = WeakReference(attacker)
    fun timeSince() = WorldThread.getCurrentCycle() - time
}

sealed interface OutgoingAttackFlag {
    data object PVP : OutgoingAttackFlag
    data object InSinglesPlus : OutgoingAttackFlag
    data object InMulti : OutgoingAttackFlag
    data object RecentlyUnderAttack : OutgoingAttackFlag
    data object RecentlyInitiatedAttacked : OutgoingAttackFlag
}
