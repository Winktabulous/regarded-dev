package com.zenyte.game.world.region

import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import java.util.*

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class DelayRepeatTask(

    private var currentTask: WorldTask,
    private var delay: Int = 0,
    private var repeats: Int = 0,
    var uuid: UUID = UUID.randomUUID(),
    var nextTask: WorldTask? = null,
    var remove: Boolean = false,
    var finishEvent: Boolean = false,
    var ongoing: Boolean = true,
    var ongoingDelay: Int = 0
) {
    var cycle = 1
    fun processedDelay() : Boolean {
        if(ongoing) return true
        return if(delay == 0) false
        else { delay--; true }
    }
    fun completed() = run { finishEvent = true }
    fun hasRepeats() : Boolean = repeats >= 1
    fun ongoing(): Boolean = ongoing
    fun hasNextTask() : Boolean = nextTask != null
    private fun runAgain() {
        repeats--
        cycle++
    }

    fun removed() = delay == 0 && repeats == 0 && nextTask != null && finishEvent

    fun shiftTask() {
        currentTask = nextTask!!
        nextTask = null
        repeats = 0
        cycle = 0
    }

    fun processOngoing() {
        if(ongoing && delay == 0) {
            WorldTasksManager.schedule(currentTask, 0, 0)
            delay = ongoingDelay
        } else if(ongoing) delay--
    }

    fun repeat() {
        runAgain()
        WorldTasksManager.schedule(currentTask, 0, 0)
    }

    fun processNormal() {
        WorldTasksManager.schedule(currentTask, 0, 0)
    }


}

