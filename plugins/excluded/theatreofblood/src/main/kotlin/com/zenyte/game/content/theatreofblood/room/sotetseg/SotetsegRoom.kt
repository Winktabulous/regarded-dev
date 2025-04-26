package com.zenyte.game.content.theatreofblood.room.sotetseg

import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.room.*
import com.zenyte.game.content.theatreofblood.room.sotetseg.npc.Sotetseg
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.dynamicregion.AllocatedArea

/**
 * @author Tommeh
 * @author Jire
 */
internal class SotetsegRoom(raid: TheatreOfBloodRaid, area: AllocatedArea, room: TheatreRoomType) :
    TheatreRoom(raid, area, room) {

    private val sotetseg = Sotetseg(this)

    fun setMazeTileIds(tile: SotetsegRoomTile) {
        val topLeft = mazeTopLeft
        val bottomRight = mazeBottomRight
        for (x in topLeft.x..bottomRight.x) {
            for (y in bottomRight.y..topLeft.y) {
                World.spawnObject(WorldObject(tile.id, 22, 0, x, y, topLeft.plane))
            }
        }
    }

    fun isInMaze(player: Player) = isInMaze(player.location)

    fun isInMaze(location: Location) = if (location.x < mazeTopLeft.x || location.x > mazeBottomRight.x) false
    else location.y >= mazeBottomRight.y && location.y <= mazeTopLeft.y

    val mazeTopLeft: Location = getLocation(3273, 4324)
    val mazeBottomRight: Location = getLocation(3286, 4310)

    override fun onLoad() = setMazeTileIds(SotetsegRoomTile.LIGHT_GREY)

    override val entranceLocation: Location = getLocation(3279, 4293, 0)

    override fun enterBossRoom(barrier: WorldObject, player: Player) {
        if (!started) {
            sotetseg.spawn()
            sotetseg.started = true
        }

        super.enterBossRoom(barrier, player)
    }

    override val vyreOrator = WorldObject(ObjectId.VYRE_ORATOR, 11, 1, getLocation(3281, 4301, 0))
    override val spectatingLocation: Location = getLocation(3272, 4301, 0)

    override var boss: TheatreBossNPC<out TheatreRoom>? = sotetseg

    override var chestInfo: ChestInfo? = SotetsegRoom.chestInfo

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) = player.y < barrier.y

    override val healthBarType: HealthBarType
        get() = if (sotetseg.isMazePhase) HealthBarType.DISABLED else HealthBarType.REGULAR

    override var nextRoomType : TheatreRoomType? = TheatreRoomType.XARPUS

    override val jailLocations = Companion.jailLocations

    private companion object {

        val chestInfo = ChestInfo(17, 5, 2)

        val jailLocations = arrayOf(
            JailLocation(6, 25),
            JailLocation(6, 26),
            JailLocation(25, 25),
            JailLocation(25, 26)
        )

    }

}