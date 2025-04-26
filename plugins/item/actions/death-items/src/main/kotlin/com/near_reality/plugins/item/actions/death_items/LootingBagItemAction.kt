package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class LootingBagItemAction : ItemActionPlugin() {
    init {
        items(LOOTING_BAG, LOOTING_BAG_22586)

        death {
            setAlwaysLostOnDeath()
            lost { yieldAll(player.lootingBag.container.items.values) }
            status { ItemDeathStatus.DELETE }
            afterDeath { player.lootingBag.clear() }
        }
    }
}
