package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.enums.UpgradeKit
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class UpgradablesItemAction : ItemActionPlugin() {
    private val items = UpgradeKit.MAPPED_VALUES.map { it.key }.toIntArray()

    init {
        items(*items)

        death {
            if (!pvp) {
                kept { yield(item) }
                status { ItemDeathStatus.KEEP_ON_DEATH }
            } else {
                val dismantleable = UpgradeKit.MAPPED_VALUES[item.id]
                lost { yield(Item(dismantleable.baseItem)) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            }
        }
    }
}

