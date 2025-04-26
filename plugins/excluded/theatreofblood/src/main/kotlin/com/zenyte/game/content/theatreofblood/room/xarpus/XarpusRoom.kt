package com.zenyte.game.content.theatreofblood.room.xarpus

import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.room.*
import com.zenyte.game.content.theatreofblood.room.xarpus.npc.Xarpus
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.dynamicregion.AllocatedArea

/**
 * @author Jire
 * @author Tommeh
 */
internal class XarpusRoom(raid: TheatreOfBloodRaid, area: AllocatedArea, room: TheatreRoomType) :
    TheatreRoom(raid, area, room) {

    private val xarpus = Xarpus(this)

    override fun enterBossRoom(barrier: WorldObject, player: Player) {
        if (!started) xarpus.spawn()

        super.enterBossRoom(barrier, player)
    }

    override fun onLoad() {}

    override val entranceLocation: Location = getLocation(3170, 4375, PLANE)
    override val vyreOrator = WorldObject(ObjectId.VYRE_ORATOR, 11, 2, getLocation(3169, 4376, PLANE))
    override val spectatingLocation: Location? = null
    override var boss: TheatreBossNPC<out TheatreRoom>? = xarpus

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) = if (barrier.rotation == 2) player.y < barrier.y else player.y > barrier.y

    override val healthBarType get() = if (xarpus.id == NpcId.XARPUS) HealthBarType.DISABLED else HealthBarType.REGULAR

    override var nextRoomType : TheatreRoomType? = TheatreRoomType.VERZIK_VITUR

    override val jailLocations = Companion.jailLocations

    companion object {

        const val PLANE = 1

        private val jailLocations = arrayOf(
            JailLocation(21, 31, Direction.EAST, plane = PLANE),
            JailLocation(21, 39, Direction.EAST, plane = PLANE),
            JailLocation(47, 31, Direction.WEST, plane = PLANE),
            JailLocation(47, 35, Direction.WEST, plane = PLANE),
            JailLocation(47, 39, Direction.WEST, plane = PLANE)
        )

    }

}