package com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe

import com.near_reality.game.content.dt2.area.VardorvisInstance
import com.near_reality.game.content.seq
import com.zenyte.game.task.TickTask
import com.zenyte.game.util.CollisionUtil
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-08
 */
class SwingingAxeTask(
    private var instance: VardorvisInstance,
    private var player: Player,
    private var axes: List<SwingingAxe>
): TickTask() {
    override fun run() {
        if (instance.killAxes) {
            instance.killAxes = false
            stop()
        }
        when (ticks++) {
            1 -> axes.forEach { it seq 10365 }
            3 -> axes.forEach {
                it.path()
                it.setTransformation(12227)
            }
            8 -> stop()
            in 4..7 -> checkOverlap()
        }
    }

    override fun stop() {
        super.stop()
        axes.forEach { it.remove() }
    }

    private fun checkOverlap() {
        for (axe in axes) {
            if (CollisionUtil.collides(axe.location, 3, player.location, 1, 0))
                instance.applyAxeDamage()
        }
    }
}