package com.zenyte.game.content.theatreofblood.room.xarpus.npc

/**
 * @author Tommeh
 * @author Jire
 */
internal abstract class XarpusPhase(protected val xarpus: Xarpus) {

    protected var ticks = 0

    fun process(): XarpusPhase {
        if (isPhaseComplete) {
            val nextPhase = advance()
            if (nextPhase != null) {
                nextPhase.onPhaseStart()
                nextPhase.onTick()
                return nextPhase
            }
        }
        onTick()
        ticks++
        return this
    }

    abstract fun onPhaseStart()
    abstract fun onTick()
    abstract val isPhaseComplete: Boolean

    abstract fun advance(): XarpusPhase?

}