package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

class VolatileNightmareStaffItemAction : ItemActionPlugin() {
    init {
        items(VOLATILE_NIGHTMARE_STAFF)

        death {
            if (pvp) {
                lost {
                    yield(Item(VOLATILE_ORB))
                    yield(Item(NIGHTMARE_STAFF))
                }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
