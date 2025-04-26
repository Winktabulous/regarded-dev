package com.near_reality.game.content.dt2.npc.whisperer.head.tasks

import com.zenyte.game.task.WorldTask
import com.zenyte.game.util.DirectionUtil
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.ForceMovement
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-23
 */
class KnockBackTask(val player: Player, val npc: NPC) : WorldTask {

    private val knockBackDistance: Int = 2
    var ticks: Int = 0
    private var destination: Location = Location(player.location)

    override fun run() {
        if (ticks == 1) {
            destination = Location(player.location)
            applyForceMovement()
            player.applyKnockbackActions(npc, destination)
        }
        else if (ticks == 3) {
            player.unlock()
            stop()
        }
        ticks++
    }

    private fun applyForceMovement() {
        val pos: Location = npc.location
        if (pos.y < destination.y) destination.setLocation(destination.x, destination.y + knockBackDistance, 0)
        else if (pos.y > destination.y) destination.setLocation(destination.x, destination.y - knockBackDistance, 0)
        else if (pos.x < destination.x) destination.setLocation(destination.x - knockBackDistance, destination.y, 0)
        else if (pos.x > destination.x) destination.setLocation(destination.x + knockBackDistance, destination.y, 0)

        val x = destination.x - pos.x
        val y = destination.y - pos.y
        val direction = DirectionUtil.getFaceDirection(x.toDouble(), y.toDouble())
        if (!destination.matches(player))
            player.forceMovement = ForceMovement(destination, 30, direction)

    }

    private fun Player.applyKnockbackActions(npc: NPC, destination: Location) {
        this.faceEntity(npc)
        this.animation = Animation.KNOCKBACK
        this.setLocation(destination)
    }
}