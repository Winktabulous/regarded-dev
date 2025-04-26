package com.near_reality.game.content.dt2.npc.whisperer

data class WhispererState(
    var usingSpecial: Boolean = false,
    var pendingTentacleAttack: Boolean = false,
    var phase: WhispererPhase = WhispererPhase.First,
) {

    fun transitionPhase() {
        when (phase) {
            WhispererPhase.First -> phase = WhispererPhase.Second
            WhispererPhase.Second -> phase = WhispererPhase.Third
            WhispererPhase.Third -> phase = WhispererPhase.Fourth
            else -> {}
        }
    }
}