package com.near_reality.plugins.area.osnr_home.obj

import com.near_reality.game.item.CustomObjectId
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

class RemnantForge : ObjectAction {
    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        if (option == "Breakdown Items") {
            GameInterface.REMNANT_EXCHANGE.open(player)
        }
        if(option == "Buy Perks") {
            GameInterface.PVPW_PERKS.open(player)
        }
        if(option == "Item Values") {
            GameInterface.PVPW_PRICES.open(player)
        }
    }

    override fun getObjects(): Array<Int> {
        return arrayOf(CustomObjectId.REMNANT_FORGE)
    }
}