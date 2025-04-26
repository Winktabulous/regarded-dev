package com.near_reality.game.content.dt2.npc.theduke

import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.npc.NPC

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class FermentationVatEntity(val spawnLoc: Location, private var progress: Int = 0, private var completion: Int = FERMENTATION_COMPLETION) {
    private var fermentationTask: TickTask? = null

    fun beginFermentation(finishTask: () -> Unit) {
        fermentationTask = object: TickTask() {
            override fun run() {
                if (progress < FERMENTATION_COMPLETION) {
                    progress = (progress + 1).coerceAtMost(FERMENTATION_COMPLETION)
                    ticks++
                } else {
                    progress = 0
                    finishTask()
                    stop()
                }
            }
        }
        WorldTasksManager.schedule(fermentationTask as TickTask, 0, 0)
    }

    companion object {
        private const val FERMENTATION_COMPLETION: Int = 18
    }
}
