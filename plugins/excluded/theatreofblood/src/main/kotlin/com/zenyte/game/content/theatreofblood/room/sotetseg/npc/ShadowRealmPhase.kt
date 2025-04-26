package com.zenyte.game.content.theatreofblood.room.sotetseg.npc

/**
 * @author Tommeh
 * @author Jire
 */
internal enum class ShadowRealmPhase(val healthPercent: Double) {

    TWO_THIRDS(66.6),
    ONE_THIRD(33.3);

    companion object {

        val phases = values()

        fun shouldInitiate(sotetseg: Sotetseg): Boolean {
            val validTargets = sotetseg.room.validTargets
            if (validTargets.size < 2) return false

            val healthPercentage = sotetseg.hitpointsAsPercentage
            for (phase in phases) {
                if (healthPercentage <= phase.healthPercent && !sotetseg.mazePhases.contains(phase.healthPercent)) {
                    sotetseg.mazePhases.add(phase.healthPercent)
                    return true
                }
            }
            return false
        }

    }

}