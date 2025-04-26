package com.zenyte.game.content.theatreofblood.room.pestilentbloat

import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.content.theatreofblood.room.*
import com.zenyte.game.content.theatreofblood.room.pestilentbloat.npc.PestilentBloat
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.utils.TimeUnit

/**
 * @author Tommeh
 * @author Jire
 */
internal class PestilentBloatRoom(
    raid: TheatreOfBloodRaid,
    area: AllocatedArea,
    room: TheatreRoomType
) : TheatreRoom(raid, area, room) {

    private val bloat = PestilentBloat(this)

    private val walkableArea = RSPolygon(
        arrayOf(
            intArrayOf(getX(3293), getY(4451)),
            intArrayOf(getX(3299), getY(4451)),
            intArrayOf(getX(3299), getY(4445)),
            intArrayOf(getX(3293), getY(4445)),
            intArrayOf(getX(3288), getY(4440)),
            intArrayOf(getX(3288), getY(4456)),
            intArrayOf(getX(3304), getY(4456)),
            intArrayOf(getX(3304), getY(4440)),
            intArrayOf(getX(3288), getY(4440)),
            intArrayOf(getX(3293), getY(4445))
        )
    )

    override fun onLoad() {
        bloat.spawn()
    }

    val randomMeatPoint: Location
        get() {
            val poly = walkableArea.polygon
            val box = poly.bounds2D
            var count = 1000
            val location = Location(0)
            do {
                if (--count <= 0)
                    throw IllegalStateException("Unable to find a valid point in polygon.")
                location.setLocation(
                    box.minX.toInt() + Utils.random(box.width.toInt()),
                    box.minY.toInt() + Utils.random(box.height.toInt()),
                    bloat.plane
                )
            } while (!poly.contains(location.x, location.y))
            return location
        }

    override fun onStart(player: Player) {
        bloat.inversePath(TimeUnit.SECONDS.toTicks(17).toInt())
        bloat.stompAttack(TimeUnit.SECONDS.toTicks(21).toInt())
    }

    override fun onComplete() {
        super.onComplete()

        for (p in entered) {
            p.packetDispatcher.resetCamera()
        }
    }

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) =
        if (barrier.rotation == 1)
            player.x > barrier.x
        else player.x < barrier.x

    override var nextRoomType : TheatreRoomType? = TheatreRoomType.THE_NYLOCAS

    override val entranceLocation: Location = getLocation(3322, 4448, 0)
    override val vyreOrator = WorldObject(ObjectId.VYRE_ORATOR_32757, 11, 2, getLocation(3309, 4445, 0))
    override val spectatingLocation: Location = getLocation(3304, 4434, 0)
    override var boss: TheatreBossNPC<out TheatreRoom>? = bloat
    override var chestInfo: ChestInfo? = PestilentBloatRoom.chestInfo
    override val healthBarType = HealthBarType.REGULAR

    override val jailLocations = Companion.jailLocations

    private companion object {

        val chestInfo = ChestInfo(5, 33, 3)

        val jailLocations = arrayOf(
            JailLocation(31, 43),
            JailLocation(32, 43),
            JailLocation(31, 20),
            JailLocation(32, 20)
        )

    }

}