package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

class DhidePouchItemAction : ItemActionPlugin() {
    init {
        items(26302, 26300)

        death {
            kept {
                yield(item)
            }
            status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}
