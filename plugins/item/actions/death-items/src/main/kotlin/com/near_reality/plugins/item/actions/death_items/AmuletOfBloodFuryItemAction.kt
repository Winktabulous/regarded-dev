package com.near_reality.plugins.item.actions.death_items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

class AmuletOfBloodFuryItemAction : ItemActionScript() {
    init {
        items(ItemId.AMULET_OF_BLOOD_FURY)

        death {
            if (pvp) {
                lost { yield(Item(ItemId.AMULET_OF_FURY)) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
