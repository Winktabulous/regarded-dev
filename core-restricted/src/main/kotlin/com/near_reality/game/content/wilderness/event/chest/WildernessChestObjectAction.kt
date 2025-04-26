package com.near_reality.game.content.wilderness.event.chest

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

@Suppress("unused")
class WildernessChestObjectAction: ObjectAction {

    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?,
    ) {
        player?: return
        /*
         * This should always be the case, but making sure in case someone spawns a different object with the same id
         */
        if (`object`is WildernessChestObject)
            WildernessChestEvent.pickupChest(player)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(WildernessChestObject.OBJECT_ID)
}
