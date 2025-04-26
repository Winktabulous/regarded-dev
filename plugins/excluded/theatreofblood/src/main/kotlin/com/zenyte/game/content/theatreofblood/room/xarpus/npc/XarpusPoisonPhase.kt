package com.zenyte.game.content.theatreofblood.room.xarpus.npc

import com.zenyte.game.content.theatreofblood.room.xarpus.XarpusRoom
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.utils.TimeUnit

/**
 * @author Tommeh
 * @author Jire
 */
internal class XarpusPoisonPhase(xarpus: Xarpus) : XarpusPhase(xarpus) {

    override fun onPhaseStart() {
        xarpus.animation = transformationAnim
        xarpus.setTransformation(NpcId.XARPUS_8340)
        xarpus.setLocation(xarpus.location.transform(-1, -1))

        val room = xarpus.room
        room.refreshHealthBar()
        if (room.raid.hardMode) {
            val fromLocation = Location(xarpus.position)
            val startX = 3
            val startY = 4
            val endX = 18
            val endY = 17

            val skipXRange = startX + 2..endX - 2
            val skipYRange = startY + 2..endY - 2

            val startYXRange = startX..startX + 2

            for (y in startY..endY) {
                for (x in startX..endX) {
                    if (y == startY && x in startYXRange) continue
                    if (x in skipXRange && y in skipYRange) continue

                    sendPoisonOrb(fromLocation, room.getBaseLocation(x, y, XarpusRoom.PLANE), null, false)
                }
            }
        }
    }

    override fun onTick() {
        if (ticks >= startDelay && ticks % attackInterval == 0) {
            val oldTarget = xarpus.combat.target
            val validTargets = xarpus.room.validTargets.filter { it != oldTarget }
            val newTarget = Utils.random(validTargets) ?: oldTarget ?: return
            xarpus.setTarget(newTarget)
            xarpus.animation = attackAnim
            sendPoisonOrb(Location(xarpus.middleLocation), newTarget.location.copy(), newTarget, false)
        }
    }

    private fun sendPoisonOrb(fromLocation: Location, toLocation: Location, newTarget: Entity?, isSplash: Boolean) {
        val poisonSplat = WorldObject(ObjectId.ACIDIC_MIASMA, 22, getSplatDirection(toLocation), toLocation)
        val delay = World.sendProjectile(
            fromLocation,
            toLocation,
            if (isSplash) splashPoisonProjectile else xarpusPoisonProjectile
        )
        val exists = xarpus.poisonSplats.contains(poisonSplat)
        WorldTasksManager.schedule({
            if (!exists) {
                World.spawnObject(poisonSplat)
                xarpus.poisonSplats.add(poisonSplat)
            }
            World.sendGraphics(poisonLandGfx, poisonSplat)
            WorldTasksManager.schedule {
                if (!exists) World.sendObjectAnimation(poisonSplat, poisonSplatObjectAnim)
                poisonNearbyPlayers(toLocation)
                if (!isSplash) {
                    val splashTarget = if (Utils.randomBoolean(2)) null else Utils.random(xarpus.room.validTargets.filter { it != newTarget })
                    if (splashTarget != null) {
                        val delay = World.sendProjectile(toLocation, splashTarget, xarpusPoisonProjectile)
                        WorldTasksManager.schedule({
                            val splashLocation = splashTarget.location.copy()
                            World.sendGraphics(poisonLandGfx, splashLocation)
                            splash(splashLocation)
                        }, delay)
                    } else {
                        splash(toLocation)
                    }
                }
            }
        }, delay)
    }

    private fun getSplatDirection(splatLocation: Location) =
        if (XarpusQuadrant.SOUTH_EAST.isInside(splatLocation, xarpus)) 0
        else if (XarpusQuadrant.SOUTH_WEST.isInside(splatLocation, xarpus)) 1
        else if (XarpusQuadrant.NORTH_WEST.isInside(splatLocation, xarpus)) 2
        else 3

    private fun poisonNearbyPlayers(splatLocation: Location) {
        for (player in xarpus.room.validTargets)
            if (player.location.withinDistance(splatLocation, 1))
                poison(xarpus, player)
    }

    private fun splash(fromLocation: Location) {
        val splashAmount = Utils.random(1, 2)
        for (i in 0 until splashAmount) {
            val splashLocation = Location(xarpus.location.random(6, 8, 6, 8, 0, 2, 0, 2))
            sendPoisonOrb(fromLocation, splashLocation, null, true)
        }
    }

    override val isPhaseComplete get() = xarpus.hitpointsAsPercentage <= 25

    override fun advance() = XarpusCounterPhase(xarpus)

    companion object {

        private val startDelay = TimeUnit.SECONDS.toTicks(4).toInt()
        private val attackInterval = TimeUnit.SECONDS.toTicks(3).toInt()

        private val transformationAnim = Animation(8061)
        private val attackAnim = Animation(8059)
        private val xarpusPoisonProjectile = Projectile(1555, 80, 0, 0, 40, 60, 64, 5)
        private val splashPoisonProjectile = Projectile(1555, 10, 0, 0, 60, 60, 0, 5)
        private val poisonLandGfx = Graphics(1556)
        private val poisonSplatObjectAnim = Animation(8068)

        fun poison(xarpus: Xarpus, player: Player) {
            WorldTasksManager.schedule {
                if (xarpus.room.isValidTarget(player))
                    player.applyHit(Hit(Utils.random(6, 8), HitType.POISON))
            }
        }

    }

}