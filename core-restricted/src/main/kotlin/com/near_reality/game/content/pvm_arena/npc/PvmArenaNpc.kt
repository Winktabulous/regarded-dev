package com.near_reality.game.content.pvm_arena.npc

import com.near_reality.game.content.pvm_arena.PvmArenaTeam

interface PvmArenaNpc {

    val config: SpawnConfig

    data class SpawnConfig(
        val team: PvmArenaTeam,
        private val maxHitPointsModifierProvider: () -> Double
    ) {
        fun transformMaxHitPoints(maxHitPoints: Int): Int {
            return (maxHitPoints * maxHitPointsModifierProvider()).toInt()
        }
    }
}
