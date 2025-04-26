package com.near_reality.game.content.dt2.npc.whisperer.head.tasks

import com.zenyte.game.task.WorldTask
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-23
 */
class DisturbOddFigureTask(val player: Player, val npc: NPC) : WorldTask {
    var ticks: Int = 0
    override fun run() {
        when (ticks++) {
            0 -> npc.animation = Animation(10227)
            3 -> {
                npc.unlock()
                stop()
            }
        }
    }
}