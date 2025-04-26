package com.zenyte.game.content.theatreofblood.room.verzikvitur.third.passivespells

import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.PassiveSpell
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.Web
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.util.DirectionUtil
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity._Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.ForceMovement
import com.zenyte.game.world.entity.pathfinding.Flags
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCTileEvent
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy

/**
 * @author Jire
 */
internal object WebPassiveSpell : PassiveSpell {

    override val nextSpell = DotPassiveSpell

    override fun VerzikVitur.cast() {
        setTransformation(8373)

        lock()
        temporaryAttributes["ignoreWalkingRestrictions"] = true
        resetWalkSteps()
        cancelCombat()
        routeEvent = NPCTileEvent(this, TileStrategy(room.getBaseLocation(29, 22))) {
            clipVerzik()
            setTransformation(8374)
            faceDirection(Direction.SOUTH)
            for (player in room.validTargets) {
                if (!player.location.withinDistance(middleLocation, 3)) continue
                player.animation = Animation.KNOCKBACK
                val pushOffLocation = player.location.transform(Direction.getDirection(middleLocation, player.location), 4)
                player.forceMovement = ForceMovement(player.location, 0, pushOffLocation, 20, OlmRoom.getMovementDirection(player.location, pushOffLocation))
                WorldTasksManager.schedule {
                    player.setLocation(pushOffLocation)
                }
            }

            WorldTasksManager.schedule {
                val anim = Animation(8127)
                val players = room.validTargets
                webLocs.clear()
                animation = anim
                val localWebs = HashSet<Location>()

                var index = 0
                for (i in 0..(anim.ceiledDuration / 600) - 7) {
                    WorldTasksManager.schedule({
                        val p = players[index]
                        if (i > 0 && i % 3 == 0) {
                            index++
                            if (index > players.lastIndex) {
                                index = 0 // reset back to first
                            }
                        }
                        faceEntity(p)
                        val middleLocation = middleLocation
                        val endLoc = p.location.copy()
                        val logicalDir = DirectionUtil.getDirection(middleLocation, endLoc)
                        val startLoc = middleLocation.transform(logicalDir, 1)

                        attemptWeb(startLoc, endLoc, webLocs, localWebs)
                        for (j in 1..2) {
                            /* also send 2 other webs near the end loc */
                            val nearEndLoc =
                                endLoc.transform(Direction.randomDirection(), Utils.random(1, 3))
                            attemptWeb(startLoc, nearEndLoc, webLocs, localWebs)
                        }
                    }, i + 3)
                }
            }
        }

        WorldTasksManager.schedule({
            unclipVerzik()
            setTransformation(8374)
            unlock()
            temporaryAttributes.remove("ignoreWalkingRestrictions")
            attackRandomTarget()
        }, 35)
    }

    private fun VerzikVitur.clipVerzik() {
        val size: Int = size
        val x: Int = x
        val y: Int = y
        val z: Int = plane
        for (x1 in x until x + size) {
            for (y1 in y until y + size) {
                World.getRegion(_Location.getRegionId(x1, y1), true).addFlag(z, x1 and 63, y1 and 63, Flags.OBJECT)
            }
        }
    }

    private fun VerzikVitur.unclipVerzik() {
        val size: Int = size
        val x: Int = x
        val y: Int = y
        val z: Int = plane
        for (x1 in x until x + size) {
            for (y1 in y until y + size) {
                World.getRegion(_Location.getRegionId(x1, y1), true).removeFlag(z, x1 and 63, y1 and 63, Flags.OBJECT)
            }
        }
    }

    private fun VerzikVitur.attemptWeb(startLoc: Location, webLoc: Location, webLocs: MutableSet<Location>, localWebs: MutableSet<Location>) {
        if (localWebs.contains(webLoc) || !World.isSquareFree(webLoc, 1)) return
        localWebs.add(webLoc)
        val projectile = Projectile(1601, 132, 0, 0, 14, 120, 0, 0)
        val delay = World.sendProjectile(startLoc, webLoc, projectile)
        WorldTasksManager.schedule({
            if (!room.completed && !isDead && !isFinished) {
                val web = Web(room, webLoc)
                web.spawn()
                webLocs.add(webLoc)
            }
        }, delay)
    }

}