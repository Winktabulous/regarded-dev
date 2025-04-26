package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.enums.ImbueableItem
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class RingOfSufferingItemAction : ItemActionPlugin() {
    init {
        items(RING_OF_SUFFERING_I, RING_OF_SUFFERING_RI)

        death {
            if (pvp) {
                val imbueable = ImbueableItem.get(item.id)
                lost { yield(Item(imbueable.normal)) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}