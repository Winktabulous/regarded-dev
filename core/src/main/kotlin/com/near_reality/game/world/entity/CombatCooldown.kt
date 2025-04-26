package com.near_reality.game.world.entity

import com.zenyte.game.util.Utils
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.areatype.AreaTypes

private const val DEFAULT_STEAL_COOLDOWN_IN_TICKS = 8
private const val PLAYER_STEAL_COOLDOWN_SINGLES_PLUS_IN_TICKS = 20

// Commonly known as PJ timer
fun getStealCooldownDurationInTicks(attacker: Entity, target: Entity): Int {
    val targetInSinglePlus = target.areaType == AreaTypes.SINGLES_PLUS
    return if (attacker is Player && target is Player && targetInSinglePlus)
        PLAYER_STEAL_COOLDOWN_SINGLES_PLUS_IN_TICKS
    else
        DEFAULT_STEAL_COOLDOWN_IN_TICKS
}

fun getAttackCooldownTicks(abstractEntity: Entity): Int {
    val currentTimeMillis = Utils.currentTimeMillis()
    val millis = abstractEntity.attackingDelay - currentTimeMillis
    val ticks = millis / 600
    val ticksCappedByArea = capByArea(abstractEntity, ticks)
    if (ticksCappedByArea != ticks)
        abstractEntity.attackingDelay = currentTimeMillis + (ticksCappedByArea * 600)
    return ticksCappedByArea.toInt()
}

fun getAttackedByCooldownTicks(abstractEntity: Entity): Int {
    val currentCycle = WorldThread.getCurrentCycle()
    val ticks = abstractEntity.attackedByDelay - currentCycle
    val ticksCappedByArea = capByArea(abstractEntity, ticks)
    if (ticksCappedByArea != ticks)
        abstractEntity.attackedByDelay = currentCycle + ticksCappedByArea
    return ticksCappedByArea.toInt()
}

fun clearAttackedByIfExpired(abstractEntity: Entity) {
    val previousAttacker = abstractEntity.attackedBy
    if (previousAttacker != null) {
        if (getAttackedByCooldownTicks(abstractEntity) <= 0) {
            if (previousAttacker.attacking != abstractEntity) {
                abstractEntity.attackedBy = null
                if (abstractEntity is Player)
                    abstractEntity.sendDeveloperMessage("Clearing attacked by because cooldown expired and last attacker is no longer attacking us")
            }
        }
    }
}

private fun capByArea(abstractEntity: Entity, ticks: Long): Long {
    return if (abstractEntity.areaType == AreaTypes.SINGLES_PLUS)
        ticks.coerceAtMost(20)
    else
        ticks.coerceAtMost(8)
}
