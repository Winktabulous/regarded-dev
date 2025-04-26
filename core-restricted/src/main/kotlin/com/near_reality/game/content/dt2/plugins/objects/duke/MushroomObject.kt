package com.near_reality.game.content.dt2.plugins.objects.duke

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

@Suppress("unused")
class MushroomObject : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if(option.equals("Pick", ignoreCase = true)) {
            val mushroom = if (`object`.id == 47528) 28345 else 28341
            player.actionManager.setAction(PickMushroomsAction(mushroom))
        }
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(47528, 47524)
    }
}

