package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

class SlayerCasketItemAction : ItemActionPlugin() {
    init {
        items(CASKET_7956)

        death {
            if (pvp) {
                setAlwaysLostOnDeath()
                lost { yield(item) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                lost { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
