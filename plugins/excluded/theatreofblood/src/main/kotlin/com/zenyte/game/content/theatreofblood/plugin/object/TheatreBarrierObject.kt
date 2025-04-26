package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Tommeh
 * @author Jire
 */
class TheatreBarrierObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        VerSinhazaArea.getArea(player)?.handleBarrier(obj, player)
    }

    override fun getObjects() = TheatreBarrierObject.objects

    private companion object {

        val objects = arrayOf(ObjectId.BARRIER_32755)

    }

}
