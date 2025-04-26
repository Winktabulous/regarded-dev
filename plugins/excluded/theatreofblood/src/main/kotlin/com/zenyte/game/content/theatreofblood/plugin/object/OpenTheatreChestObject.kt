package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.room.ChestInfo
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class OpenTheatreChestObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        VerSinhazaArea.getArea(player) ?: return

        GameInterface.TOB_CHEST_SUPPLIES.open(player)
    }

    override fun getObjects() = OpenTheatreChestObject.objects

    private companion object {

        val objects = arrayOf(ChestInfo.OPEN_CHEST_OBJECT_ID)

    }

}