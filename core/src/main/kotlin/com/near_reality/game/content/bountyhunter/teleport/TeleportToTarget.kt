package com.near_reality.game.content.bountyhunter.teleport

import com.near_reality.game.content.bountyhunter.getTarget
import com.near_reality.game.content.bountyhunter.getWildernessLevel
import com.near_reality.game.content.bountyhunter.isBountyPaired
import com.near_reality.game.content.offset
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import java.util.*
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-14
 */
object TeleportToTarget {

    fun canTeleportToTarget(player: Player): Boolean {
        if (!player.isBountyPaired()) return false
        val target = player.getTarget()
        return if (target.isPresent) {
            return target.get().getWildernessLevel() > 0
        } else { false }
    }

    fun teleportToTarget(player: Player) {
        val target = player.getTarget()
        if(target.isPresent) {
            val landingLocation: Location = getTeleportToTargetLocation(target.get())
            player.teleport(landingLocation)
        }
    }

    private fun getTeleportToTargetLocation(target: Player?): Location {
        val targetLocation = target?.location?.offset(Pair(-6, -6))
        return getLandingLocation(targetLocation)
    }

    private fun getLandingLocation(location: Location?): Location {
        val xOffset = Random.nextInt(0, 12)
        val yOffset = Random.nextInt(0, 12)
        val tempLocation = Location(location?.offset(Pair(xOffset, yOffset)))
        if (!isValidTile(tempLocation))
            return getLandingLocation(location)
        return tempLocation
    }

    private fun isValidTile(location: Location): Boolean {
        return !World.isTileFree(location, 0) ||
                World.getObjectWithType(location, 10) != null ||
                World.getObjectWithType(location, 11) != null
    }

}