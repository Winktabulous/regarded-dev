package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * @author Kris | 12/06/2022
 */
class FerociousGlovesItemAction : ItemActionPlugin() {
    init {
        items(ItemId.FEROCIOUS_GLOVES)

        death {
            if (pvp) {
                lost { yield(Item(ItemId.HYDRA_LEATHER)) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                lost { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
