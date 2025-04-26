package com.near_reality.game.content.gauntlet.`object`

import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

@Suppress("UNUSED")
class GauntletCrystalSingingRecipes : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        if (option == "Read") {
            player.interfaceHandler.sendInterface(
                InterfacePosition.CENTRAL, 640
            )
        }
    }

    override fun getObjects() = arrayOf(
        ObjectId.CRYSTAL_SINGING_RECIPES, // corrupted
        ObjectId.CRYSTAL_SINGING_RECIPES_36075
    )
}
