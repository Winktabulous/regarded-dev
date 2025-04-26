package com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.screech

import com.near_reality.game.content.*
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.WhispererSpecial
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

object ScreechSpecialAttack : WhispererSpecial {

    // ArrayList to hold Pillar NPC
    val pillars = mutableListOf<NPC>()

    private val pillarOffsets = arrayOf(
        -9 to 6,
        -8 to 2,
        -6 to 3,
        -2 to 4,
        2 to 4,
        6 to 3,
        8 to 2,
        9 to 6
    )

    /**
     * Creates the pillar NPCs used during the special attack.
     */
    private fun spawn(centerTile: Location) {
        val pillar = FloatingPillar(centerTile)
            pillar.spawn()

        pillars.add(pillar)
        World.sendGraphics(Graphics(2450), centerTile)
    }

    override fun setup(whisperer: WhispererCombat, player: Player) {
        pillars.clear()
        whisperer.radius = 0
        val spawnLocation = whisperer.spawnLocation
        schedule(object: TickTask() {
            override fun run() {
                when (ticks++) {
                    0 -> whisperer.setLocation(spawnLocation offset Pair(0, -10))
                    2 -> whisperer faceDir North
                    3 -> whisperer seq 10250
                    4 -> stop()
                }
            }
        }, 0, 0)
        pillarOffsets.map { spawn(((spawnLocation offset Pair(0, -10)) offset it)) }
    }

    override fun execute(whisperer: WhispererCombat, target: Player) {
        schedule(object: TickTask() {
            override fun run() {
                // check if the npc is dead
                if (whisperer.isDead || whisperer.isFinished) stop()
                // check if the target is dead
                if (target.isDead || target.isFinished) stop()
                // check if the Pillars are dead
                if (pillars.isEmpty()) {
                    // Move whisperer to the centre of the arena
                    whisperer seq 10252
                    whisperer.state.transitionPhase()
                    whisperer.state.usingSpecial = false
                    whisperer.setLocation(whisperer.spawnLocation)
                    stop()
                }
                // Attack tick
                if (ticks++ % 10 == 0)
                    schedule(ScreechAttackTask(whisperer, target),0, 0)
            }
        }, 10, 0)
    }
}