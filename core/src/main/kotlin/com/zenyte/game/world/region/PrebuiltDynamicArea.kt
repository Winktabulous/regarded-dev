package com.zenyte.game.world.region

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.PrebuiltDynamicAreaManager.pendingAdditions
import com.zenyte.game.world.region.PrebuiltDynamicAreaManager.pendingRemovals
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.UUID

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
abstract class PrebuiltDynamicArea(region: Int, allocatedArea: AllocatedArea = MapBuilder.findEmptyChunk(9,9), val uuid: UUID = UUID.randomUUID()) : DynamicArea (allocatedArea, region) {

    var registered = false

    val tasks : ObjectArrayList<DelayRepeatTask> = ObjectArrayList()
    val npcs : ObjectArrayList<NPC> = ObjectArrayList()

    fun addEvent(task: DelayRepeatTask) {
        if(!registered)
           registerInstance(this)
        pendingAdditions.add(task)
        tasks.add(task)
    }

    fun removeEvent(task: DelayRepeatTask) {
        pendingRemovals.add(task.uuid)
    }

    override fun leave(player: Player, logout: Boolean) {
        if(this.players.isEmpty())
            PrebuiltDynamicAreaManager.deregister(this)
        player.mapInstance = null
    }

    fun finishTasks() {
        for(task in tasks)
            removeEvent(task)
        for(npc in npcs)
            npc.finish()
    }

    fun addNpc(spawn: NPC) {
        npcs.add(spawn)
    }

    companion object {
        fun registerInstance(reference: PrebuiltDynamicArea) {
            PrebuiltDynamicAreaManager.register(reference)
        }
    }
}
