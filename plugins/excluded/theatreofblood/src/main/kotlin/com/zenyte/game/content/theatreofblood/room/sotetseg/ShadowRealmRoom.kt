package com.zenyte.game.content.theatreofblood.room.sotetseg

import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.party.RaidingParty.Companion.getPlayer
import com.zenyte.game.content.theatreofblood.room.HealthBarType
import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.TheatreRoom
import com.zenyte.game.content.theatreofblood.room.TheatreRoomType
import com.zenyte.game.content.theatreofblood.room.sotetseg.npc.Sotetseg
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.slf4j.LoggerFactory

/**
 * @author Tommeh
 * @author Jire
 */
internal class ShadowRealmRoom(
    raid: TheatreOfBloodRaid,
    area: AllocatedArea,
    room: TheatreRoomType,
    val player: Player,
    sotetseg: Sotetseg
) : TheatreRoom(raid, area, room) {

    var mazeTopLeft: Location = Location(3354, 4325, 3)
    var mazeBottomRight: Location = Location(3367, 4311, 3)

    var mazePath: ObjectSet<Location>? = null

    private var ticks = 0

    init {
        mazeTopLeft = getLocation(mazeTopLeft)
        mazeBottomRight = getLocation(mazeBottomRight)
        val path = generatePath(mazeTopLeft, mazeBottomRight)
        if (path == null) {
            mazePath = generateEmergencyPath(mazeTopLeft, mazeBottomRight)
            for (m in raid.party.members) {
                val member = getPlayer(m) ?: continue
                member.sendMessage(Colour.RED.wrap("Attention: AN ERROR OCCURRED WHEN GENERATING SOTETSEG MAZE, PLEASE CONTACT AN ADMIN"))
            }
            logger.error("Error occurred when generating Sotetseg maze") // TODO add some more data
        } else {
            mazePath = path
        }
    }

    override fun process() {
        super.process()
        if (ticks++ == 6) {
            ticks = 0
            randomDamage()
        }
    }

    fun shouldStartMazeStorm() = player.y > mazeBottomRight.y + 2

    private fun randomDamage() {
        if (completed) return
        player.graphics = randomDamageGfx
        WorldTasksManager.schedule({
            if (completed) return@schedule
            player.applyHit(Hit(Utils.random(1, 3), HitType.REGULAR))
        }, 2)
    }

    override val entranceLocation: Location? = null
    override val vyreOrator: WorldObject? = null
    override val spectatingLocation: Location? = null
    override var boss: TheatreBossNPC<out TheatreRoom>? = sotetseg

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) = false

    override fun constructed() {
        for (mazeTile in mazePath!!)
            World.spawnObject(WorldObject(SotetsegRoomTile.RED.id, 22, 0, mazeTile))
        player.setLocation(getLocation(3360, 4309, 3))
        player.cancelCombat()
        refreshHealthBar(player)
    }

    override fun enter(player: Player) {}

    override fun leave(player: Player, logout: Boolean) {}

    override val healthBarType = HealthBarType.DISABLED

    override var nextRoomType: TheatreRoomType? = null

    companion object {

        private val logger = LoggerFactory.getLogger(ShadowRealmRoom::class.java)

        private const val MAZE_WIDTH = 14
        private const val MAZE_HEIGHT = 15

        private val randomDamageGfx = Graphics(1608)

        fun generatePath(topLeft: Location, bottomRight: Location): ObjectLinkedOpenHashSet<Location>? {
            val yIndexOffset = 2
            val maxXChange = 5
            val tiles = ArrayList<Location>()
            var yIndex: Int
            var xIndex = Utils.random(0, MAZE_WIDTH - 1)
            val startTile = topLeft.transform(xIndex, -MAZE_HEIGHT + 1, 0)
            tiles.add(startTile) // start
            tiles.add(startTile.transform(0, 1, 0)) // start
            yIndex = 1
            var iterations = 0
            while (yIndex < MAZE_HEIGHT - 1) {
                val newXIndex =
                    Utils.random(
                        0.coerceAtLeast(xIndex - maxXChange),
                        (xIndex + maxXChange).coerceAtMost(MAZE_WIDTH - 1)
                    )
                if (xIndex > newXIndex) {
                    for (x in newXIndex.coerceAtLeast(xIndex) downTo newXIndex.coerceAtMost(xIndex)) {
                        tiles.add(translateTile(topLeft, bottomRight, x, yIndex)) // fill in X changes
                    }
                } else {
                    for (x in newXIndex.coerceAtMost(xIndex)..newXIndex.coerceAtLeast(xIndex)) {
                        tiles.add(translateTile(topLeft, bottomRight, x, yIndex)) // fill in X changes
                    }
                }
                var newYIndex: Int =
                    yIndex + if (Utils.random(1) == 0) yIndexOffset else yIndexOffset * 2 // Y always increases
                newYIndex = newYIndex.coerceAtMost(MAZE_HEIGHT - 1)
                for (y in yIndex until newYIndex) {
                    tiles.add(translateTile(topLeft, bottomRight, newXIndex, y)) // fill in Y changes
                }

                // update current indices
                xIndex = newXIndex
                yIndex = newYIndex
                iterations++
                if (iterations > 1000) {
                    // if for some reason the while-loop continues far over the time it should take, return null so it can be handled
                    return null
                }
            }
            tiles.add(tiles[tiles.size - 1].transform(0, 1, 0)) // last tile
            return ObjectLinkedOpenHashSet(tiles)
        }

        private fun generateEmergencyPath(
            topLeft: Location,
            @Suppress("UNUSED_PARAMETER") bottomRight: Location
        ): ObjectLinkedOpenHashSet<Location> {
            val tiles = ArrayList<Location>()
            val endY = topLeft.y
            val startY = topLeft.y - (MAZE_HEIGHT - 1)
            for (y in startY..endY) tiles.add(Location(topLeft.x, y, topLeft.plane))
            return ObjectLinkedOpenHashSet(tiles)
        }

        private fun translateTile(topLeft: Location, bottomRight: Location, x: Int, y: Int) =
            Location(
                topLeft.transform(x, 0, 0).x,
                bottomRight.transform(0, y, 0).y,
                topLeft.plane
            )

    }

}