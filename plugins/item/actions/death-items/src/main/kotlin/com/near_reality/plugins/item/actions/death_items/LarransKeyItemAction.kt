package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.content.lootkeys.LootkeyConstants
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class LarransKeyItemAction : ItemActionPlugin() {
    init {
        items(LARRANS_KEY, LootkeyConstants.LOOT_KEY_ITEM_ID)

        death {
            setAlwaysLostOnDeath()
            if (pvp) {
                lost { yield(item) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}