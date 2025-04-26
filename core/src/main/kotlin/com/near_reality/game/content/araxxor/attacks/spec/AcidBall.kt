package com.near_reality.game.content.araxxor.attacks.spec

import com.near_reality.game.content.South
import com.near_reality.game.content.araxxor.Araxxor
import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.attacks.Attack
import com.near_reality.game.content.offset
import com.near_reality.game.content.seq
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.ACIDIC_ARAXYTE_BALL
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-25
 */
class AcidBall(
    val instance: AraxxorInstance
): NPC(
    ACIDIC_ARAXYTE_BALL,
    instance.araxxor.middleLocation,
    true
), Attack {

    private val projectileBlob: Projectile = Projectile(2924, 64, 32, 64, 0)
    private var moveDirection: Direction = South

    init {
        radius = 0
        isRun = true
    }

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null || araxxor.isDead || araxxor.isFinished) return
        araxxor seq 11484
        val spawnLocation = araxxor.middleLocation offset Pair(-1, 0)
        this.setLocation(spawnLocation)
        this.spawn()
        moveDirection = getMovingDirection(target.location, middleLocation offset Pair(-1, 0))
        faceDirection(moveDirection)
    }

    private fun getMovingDirection(from: Location, to: Location): Direction {
        val x = from.x - to.x
        val y = from.y - to.y

        if (x < 0 && y == 0)
            return Direction.WEST
        else if (x > 0 && y == 0)
            return Direction.EAST

        else if (x == 0 && y < 0)
            return Direction.SOUTH
        else if (x == 0 && y > 0)
            return Direction.NORTH

        else if (x < 0 && y < 0)
            return Direction.SOUTH_WEST
        else if (x > 0 && y < 0)
            return Direction.SOUTH_EAST

        else if (x < 0)
            return Direction.NORTH_WEST
        else
            return Direction.NORTH_EAST
    }

    private fun explode() {
        repeat(9) {
            val landing = instance.getAcidSplatterLocation(location)
            val delay = World.sendProjectile(location, landing, projectileBlob)
            World.sendGraphics(Graphics(2923, delay, 0), landing)
            instance.spawnAcidPool(landing)
            this.remove()
        }
    }

    override fun processNPC() {
        super.processNPC()
        val nextLoc: Location = middleLocation.transform(moveDirection.offsetX, moveDirection.offsetY)
        val locAfter: Location = nextLoc.transform(moveDirection.offsetX, moveDirection.offsetY)
        instance.spawnAcidPool(middleLocation)
        val target = instance.araxxor.attacking
        if (target != null && target is Player) {
            if (location.withinDistance(target.location, 2))
                target.applyHit(Hit(4, HitType.VENOM))
        }
        addWalkStep(locAfter.x, locAfter.y, nextLoc.x, nextLoc.y, false)
        if (World.getObjectWithId(locAfter, 6926) != null ||
            World.getObjectWithId(locAfter, 54165) != null ||
            World.getObjectWithId(locAfter, 54166) != null ||
            World.getObjectWithId(locAfter, 54167) != null ||
            World.getObjectWithId(locAfter, 54168) != null ||
            World.getObjectWithId(locAfter, 54172) != null ||
            World.getObjectWithId(locAfter, 54173) != null ||
            World.getObjectWithId(locAfter, 54247) != null ||
            World.getObjectWithId(locAfter, 54248) != null ||
            World.getObjectWithId(locAfter, 54250) != null ||
            World.getObjectWithId(locAfter, 54251) != null ||
            World.getObjectWithId(locAfter, 54254) != null ||
            World.getObjectWithId(locAfter, 54256) != null ||
            World.getObjectWithId(locAfter, 54258) != null)
            explode()
    }

    override fun setRespawnTask() {}
}