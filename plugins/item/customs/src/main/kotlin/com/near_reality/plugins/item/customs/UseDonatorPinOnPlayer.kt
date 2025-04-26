package com.near_reality.plugins.item.customs

import com.near_reality.game.content.middleman.MiddleManConstants
import com.near_reality.game.content.middleman.MiddleManManager
import com.near_reality.game.content.middleman.middleManController
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ItemOnPlayerPlugin
import com.zenyte.game.world.entity.player.Player

/**
 * Represents an [ItemOnPlayerPlugin] that handles the action of
 * using a donator-pin item on a player.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class UseDonatorPinOnPlayer : ItemOnPlayerPlugin {

    override fun handleItemOnPlayerAction(player: Player, item: Item, slot: Int, target: Player) {
        player.middleManController.openTradeRequestInterface(
            offerItemId = item.id,
            offerAmount = 1,
            targetDisplayName = target.name
        )
    }

    override fun getItems(): IntArray =
        MiddleManConstants.donatorPinItemIds
}
