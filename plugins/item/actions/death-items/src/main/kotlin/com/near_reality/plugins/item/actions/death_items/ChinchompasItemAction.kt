package com.near_reality.plugins.item.actions.death_items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId.BLACK_CHINCHOMPA
import com.zenyte.game.item.ItemId.CHINCHOMPA_10033
import com.zenyte.game.item.ItemId.RED_CHINCHOMPA_10034
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * @author Kris | 12/06/2022
 */
class ChinchompasItemAction : ItemActionScript() {
    init {
        items(CHINCHOMPA_10033, RED_CHINCHOMPA_10034, BLACK_CHINCHOMPA)

        death {
            if (pvp) {
                lost { yield(item) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                status { ItemDeathStatus.DELETE }
            }
        }
    }
}
