package com.zenyte.game.content.skills.agility.shortcut

import com.zenyte.game.content.skills.agility.Shortcut
import com.zenyte.game.content.skills.agility.shortcut.HoleShortcut.Instance.*
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.ForceMovement
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Represents a hole type-shortcut.
 *
 * TODO: dump proper force movement values from OSRS.
 *
 * @author Stan van der Bend
 * @author Kris | 26. aug 2018 : 18:41:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
class HoleShortcut : Shortcut {

    companion object {

        private val DUCK = Animation(2589)
        private val INVISIBLE = Animation(2590)
        private val EMERGE = Animation(2591)

        private val all = listOf(PiscatorisFishingColonyTunnel, Home1, Home2, Home3, Home4, Home5, Home6)

        private fun WorldObject.getHoleInstance() =
            all.find { it.hash1 == positionHash || it.hash2 == positionHash }
                ?: error("Missing HoleShortcut instance for object $this")
    }

    override fun getObjectIds() = intArrayOf(ObjectId.HOLE_12656)
    override fun getLevel(`object`: WorldObject) = 0
    override fun getDuration(success: Boolean, `object`: WorldObject) = 5
    override fun getSuccessXp(`object`: WorldObject) = 0.0
    override fun getRouteEvent(player: Player, `object`: WorldObject): Location =
        `object`.getHoleInstance().getTargetLocation(`object`.positionHash)

    override fun startSuccess(player: Player, `object`: WorldObject) {
        val instance = `object`.getHoleInstance()
        val direction = `object`.positionHash == instance.hash1
        player.animation = DUCK
        player.forceMovement = if (direction) instance.toLoc2 else instance.toLoc1
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                when (ticks) {
                    1 -> player.animation = INVISIBLE
                    3 -> player.animation = EMERGE
                    5 -> {
                        player.setLocation(if (direction) instance.loc2 else instance.loc1)
                        stop()
                    }
                }
                ticks++
            }
        }, 0, 0)
    }

    /**
     * TODO: this can be simplified
     */
    sealed class Instance(
        val hash1: Int,
        val hash2: Int,
        val loc1: Location,
        val loc2: Location,
        val direction1: Int,
        val direction2: Int
    ) {
        val toLoc1 = ForceMovement(loc1, 180, direction1)
        val toLoc2 = ForceMovement(loc2, 180, direction2)

        fun getTargetLocation(positionHash: Int) =
            when(positionHash) {
                hash1 -> loc1
                hash2 -> loc2
                else -> error("Unexpected position hash (provided = $positionHash, expected either $hash1 or $hash2)")
            }

        object PiscatorisFishingColonyTunnel :Instance(
            hash1 = Location(2344, 3651, 0).positionHash,
            hash2 = Location(2344, 3654, 0).positionHash,
            loc1 = Location(2344, 3650, 0),
            loc2 = Location(2344, 3655, 0),
            direction1 = ForceMovement.SOUTH,
            direction2 = ForceMovement.NORTH
        )
        object Home1 : Instance(
            hash1 =  Location(3050, 3506, 0).positionHash,
            hash2 =  Location(3050, 3510, 0).positionHash,
            loc1 =  Location(3050, 3505, 0),
            loc2 =  Location(3050, 3511, 0),
            direction1 = ForceMovement.SOUTH,
            direction2 = ForceMovement.NORTH
        )
        object Home2 : Instance(
            hash1 =  Location(3132, 3470, 0).positionHash,
            hash2 =  Location(3136, 3470, 0).positionHash,
            loc1 =  Location(3131, 3470, 0),
            loc2 =  Location(3137, 3470, 0),
            direction1 = ForceMovement.WEST,
            direction2 = ForceMovement.EAST
        )
        object Home3 : Instance(
            hash1 =  Location(3108, 3425, 0).positionHash,
            hash2 =  Location(3108, 3429, 0).positionHash,
            loc1 =  Location(3108, 3424, 0),
            loc2 =  Location(3108, 3430, 0),
            direction1 = ForceMovement.SOUTH,
            direction2 = ForceMovement.NORTH
        )
        object Home4 : Instance(
            hash1 =  Location(3042, 3444, 0).positionHash,
            hash2 =  Location(3046, 3444, 0).positionHash,
            loc1 =  Location(3041, 3444, 0),
            loc2 =  Location(3047, 3444, 0),
            direction1 = ForceMovement.WEST,
            direction2 = ForceMovement.EAST
        )
        object Home5 : Instance(
            hash1 =  Location(3004, 3447, 0).positionHash,
            hash2 =  Location(3004, 3451, 0).positionHash,
            loc1 =  Location(3004, 3446, 0),
            loc2 =  Location(3004, 3452, 0),
            direction1 = ForceMovement.SOUTH,
            direction2 = ForceMovement.NORTH
        )
        object Home6 : Instance(
            hash1 =  Location(2983, 3496, 0).positionHash,
            hash2 =  Location(2987, 3496, 0).positionHash,
            loc1 =  Location(2982, 3496, 0),
            loc2 =  Location(2988, 3496, 0),
            direction1 = ForceMovement.WEST,
            direction2 = ForceMovement.EAST
        )
    }
}
