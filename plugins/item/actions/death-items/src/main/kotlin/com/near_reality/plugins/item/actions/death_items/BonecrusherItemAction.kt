package com.near_reality.plugins.item.actions.death_items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * @author Kris | 12/06/2022
 */
class BonecrusherItemAction : ItemActionScript() {
    init {
        items(ItemId.BONECRUSHER)

        death {
            if (!deepWilderness) {
                kept {
                    yield(item)
                }
                status { ItemDeathStatus.KEEP_ON_DEATH }
            } else {
                status { ItemDeathStatus.DELETE }
            }
        }
    }
}
