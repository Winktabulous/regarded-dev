package com.zenyte.game.world.region

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
object PrebuiltDynamicAreaManager {



    @JvmStatic private val registeredEventHosts: Object2ObjectArrayMap<UUID, PrebuiltDynamicArea> = Object2ObjectArrayMap()
    @JvmStatic private val dynamicTasks : Object2ObjectArrayMap<UUID, DelayRepeatTask> = Object2ObjectArrayMap()
    @JvmStatic val pendingRemovals: ObjectArrayList<UUID> = ObjectArrayList()
    @JvmStatic val pendingAdditions: ObjectArrayList<DelayRepeatTask> = ObjectArrayList()

    @JvmStatic fun process() {
        // First we remove all pending removals from the task queue. Any new style dynamic area
        // will remove any associated tasks one tick before region destruction
        for(uid in pendingRemovals) {
            dynamicTasks.remove(uid)
        }
        // We clear the removals for next time, at this point in world thread we are still frozen
        pendingRemovals.clear()

        // We add our new tasks to the register
        for(task in pendingAdditions) {
            dynamicTasks[task.uuid] = task
        }
        //As like before, we clear our queue now
        pendingAdditions.clear()

        for(task in dynamicTasks.values) {
            // First we check for delays and process those as they are hard pauses in the script
            if(task.processedDelay()) continue
            // Second we check for a needed task shuffle and change those before running
            if(task.hasNextTask()) task.shiftTask()
            // Third we process any repeat tasks which will not be removed this cycle
            if(task.hasRepeats()) { task.repeat(); continue }
            // Fourth we process ongoing tasks and their own delay mechanics
            if(task.ongoing()) { task.processOngoing(); continue }
            // Finally we process any remaining tasks that are not ongoing and do not have a delay, then add for removal
            task.processNormal()
            pendingRemovals.add(task.uuid)
        }

    }

    fun register(reference: PrebuiltDynamicArea) {

        registeredEventHosts[reference.uuid] = reference
    }

    fun deregister(reference: PrebuiltDynamicArea) {
        reference.finishTasks()
        registeredEventHosts.remove(reference.uuid)
    }
}