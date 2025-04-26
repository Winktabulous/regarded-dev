package com.near_reality.game.content.dt2.npc.theduke

import com.near_reality.game.content.dt2.area.DukeSucellusInstance
import com.near_reality.game.content.dt2.npc.whisperer.WhispyUtils.rand
import com.zenyte.game.world.World
import it.unimi.dsi.fastutil.objects.ObjectArrayList

data class ExtremityGroup(
    var side: ExtremitySide,
    var arena: DukeSucellusInstance,
    var members: List<Extremity>,
    var cooldown: Int = rand(17, 35)
) {
    fun process() {
        if(cooldown == 0) {
            activateTask()
            cooldown = rand(25, 45) + 15
        } else {
            cooldown++
        }
    }

    private fun activateTask() {

    }

    fun awaken() {
        for (member in members) {
            World.spawnObject(member.awakened())
        }
    }

    fun sleep() {
        for (member in members) {
            World.spawnObject(member.sleeping())
        }
    }
}