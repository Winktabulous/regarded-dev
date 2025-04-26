package com.zenyte.game.content.theatreofblood.room

import com.zenyte.game.content.theatreofblood.interfaces.TheatreOfBloodSuppliesInterface.Companion.tobPoints
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.model.HintArrow
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.utils.TextUtils

/**
 * @author Jire
 */
internal data class ChestInfo(
    val localX: Int,
    val localY: Int,
    val rotation: Int
) {

    fun spawnInArea(theatreRoom: TheatreRoom) {
        val worldObject = WorldObject(
            CLOSED_CHEST_OBJECT_ID,
            WorldObject.DEFAULT_TYPE,
            rotation,
            theatreRoom.getBaseLocation(localX, localY)
        )

        World.spawnObject(worldObject)

        val hintArrow = HintArrow(worldObject.x, worldObject.y, worldObject.plane.toByte())
        theatreRoom.raid.party.players.forEach { player ->
            player.sendMessage("The Vampyres are impressed by your prowess. " + "${Colour.RED.wrap("Check the chest")} to see how many points they've granted you.")
            player.packetDispatcher.sendHintArrow(hintArrow)
        }
    }

    companion object {

        internal const val CLOSED_CHEST_OBJECT_ID = ObjectId.CHEST_32758
        internal const val OPEN_CHEST_OBJECT_ID = ObjectId.CHEST_32759

    }

}