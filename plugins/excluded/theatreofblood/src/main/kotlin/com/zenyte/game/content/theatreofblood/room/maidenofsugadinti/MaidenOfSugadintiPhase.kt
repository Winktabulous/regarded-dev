package com.zenyte.game.content.theatreofblood.room.maidenofsugadinti

import com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.npc.MaidenOfSugadinti
import com.zenyte.game.world.entity.npc.NpcId

/**
 * @author Tommeh
 * @author Jire
 */
internal enum class MaidenOfSugadintiPhase(val percent: Int, val npcId: Int) {

    FIRST(100, NpcId.THE_MAIDEN_OF_SUGADINTI),
    SECOND(70, NpcId.THE_MAIDEN_OF_SUGADINTI_8361),
    THIRD(50, NpcId.THE_MAIDEN_OF_SUGADINTI_8362),
    FOURTH(30, NpcId.THE_MAIDEN_OF_SUGADINTI_8363),
    DYING(0, NpcId.THE_MAIDEN_OF_SUGADINTI_8364),
    DEAD(0, NpcId.THE_MAIDEN_OF_SUGADINTI_8365);

    companion object {

        private val phases: Array<MaidenOfSugadintiPhase> = entries.toTypedArray()

        val MaidenOfSugadinti.appropriateNewPhase: MaidenOfSugadintiPhase
            get() {
                val hitpointsAsPercentage = hitpointsAsPercentage
                for (i in phases.indices.reversed()) {
                    val phase = phases[i]
                    if (hitpointsAsPercentage <= phase.percent)
                        return phase
                }
                return DYING
            }

    }

}