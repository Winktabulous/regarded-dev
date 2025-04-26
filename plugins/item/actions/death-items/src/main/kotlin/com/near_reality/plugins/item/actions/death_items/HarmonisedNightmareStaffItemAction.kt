package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

class HarmonisedNightmareStaffItemAction : ItemActionPlugin() {
    init {
        items(ItemId.HARMONISED_NIGHTMARE_STAFF)

        death {
            if (pvp) {
                lost {
                    yield(Item(ItemId.HARMONISED_ORB))
                    yield(Item(ItemId.NIGHTMARE_STAFF))
                }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
