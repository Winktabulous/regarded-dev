package com.near_reality.game.util

class Ticker(var ticks: Int, var active : Boolean = true, var runnable: () -> Unit = {}, val resetAutomatically: Boolean = false, val defaultsToInactive: Boolean = false) {
    private val originalTicks : Int = ticks

    val finished: Boolean
        get() = ticks == 0

    fun tick() {
        if(active)
            ticks--
        if(finished) {
            runnable()
            if(resetAutomatically) {
                if(defaultsToInactive) resetAsInactive() else reset()
            }
        }
    }

    fun setNewRunnable(newRunnable: () -> Unit = {}) = run { this.runnable = newRunnable }

    fun reset() {
        ticks = originalTicks
    }

    fun resetWithRunnable(newRunnable: () -> Unit = {}) = run { reset(); active = true; this.runnable = newRunnable }

    fun resetAsInactive() {
        reset()
        active = false
    }

}
