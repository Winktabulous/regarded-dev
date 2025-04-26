package com.near_reality.game.content.dt2.npc.whisperer.attacks.special

import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.seq
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player

interface WhispererSpecial {

    fun setup(whisperer: WhispererCombat, player: Player)

    fun startTimer(whisperer: WhispererCombat, player: Player) {
        whisperer seq 10251
        schedule(object : TickTask() {
            var barDelay = 0
            override fun run() {
                whisperer seq 10251
                if (whisperer.isDead) stop()
                when (barDelay) {
                    15 -> stop()
                    in 1..15 -> barDelay++
                }
            }

            override fun stop() {
                WorldTask.defaultStop(this)

                whisperer seq Animation.STOP.id
                whisperer.state.usingSpecial = false
                execute(whisperer, player)
            }
        }, 2, 0)
    }

    fun execute(whisperer: WhispererCombat, target: Player)
}