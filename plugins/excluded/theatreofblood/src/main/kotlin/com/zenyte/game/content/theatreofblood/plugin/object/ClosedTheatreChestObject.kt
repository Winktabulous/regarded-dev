package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.room.ChestInfo
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class ClosedTheatreChestObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        VerSinhazaArea.getArea(player) ?: return

        World.replaceObject(obj, obj.transform(ChestInfo.OPEN_CHEST_OBJECT_ID))

        player.sendMessage("You open the chest. It glimmers with rewards inside.")
    }

    override fun getObjects() = ClosedTheatreChestObject.objects

    private companion object {

        val objects = arrayOf(ChestInfo.CLOSED_CHEST_OBJECT_ID)

    }

}