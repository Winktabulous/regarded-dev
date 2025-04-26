package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * @author Kris | 12/06/2022
 */
class GraniteCannonballsItemAction : ItemActionPlugin() {
    init {
        items(ItemId.GRANITE_CANNONBALL)

        death {
            if (pvp) {
                lost { yield(Item(ItemId.CANNONBALL, item.amount)) }
            } else {
                kept { yield(item) }
            }
            status {
                if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE
            }
        }
    }
}
