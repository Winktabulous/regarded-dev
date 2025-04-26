package com.near_reality.plugins.area.osnr_home.obj

import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

class TabletLectern : ObjectAction {

    private fun Player.openTabletCreator() {
        interfaceHandler.sendInterface(InterfacePosition.CENTRAL, 79)
        varManager.sendVar(261, 3)
        varManager.sendVar(262, 3)
    }

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (option == "Create-tablet") {
            player.openTabletCreator()
        }
    }

    override fun getObjects() = objectIDs

    private companion object {
        val objectIDs = arrayOf(ObjectId.LECTERN_18245)
    }

}