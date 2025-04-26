package com.zenyte.game.content.theatreofblood.room.xarpus.npc

/**
 * @author Tommeh
 * @author Jire
 */
internal object XarpusExhumedPhaseFactory {

    fun getPhase(xarpus: Xarpus, partySize: Int): XarpusExhumedPhase = when (partySize) {
        1 -> XarpusExhumedPhase(xarpus, 7, 20, 13, 11)
        2 -> XarpusExhumedPhase(xarpus, 8, 16, 8, 11)
        3 -> XarpusExhumedPhase(xarpus, 12, 12, 8, 11)
        4 -> XarpusExhumedPhase(xarpus, 15, 9, 4, 11)
        5 -> XarpusExhumedPhase(xarpus, 18, 6, 4, 11)
        else -> throw IllegalArgumentException("Invalid party size of $partySize")
    }

}