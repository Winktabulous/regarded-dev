package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.degradableitems.RepairableItem
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class RepairableItemsItemAction : ItemActionPlugin() {
    val breakableItems = RepairableItem.VALUES.filterNot { it.isTradeable }.map { it.ids.first() }

    init {
        items(breakableItems)

        death {
            status {
                when {
                    pvp -> ItemDeathStatus.KEEP_DOWNGRADED
                    else -> ItemDeathStatus.GO_TO_GRAVESTONE
                }
            }
            if (pvp) {
                lost {
                    val receivedCoins = (item.definitions.price * 0.12).toInt()
                    if (receivedCoins > 0) {
                        yield(Item(COINS_995, receivedCoins))
                    }
                }
                kept {
                    yield(Item(RepairableItem.getItem(item).ids[1]))
                }
            } else {
                lost { yield(item) }
            }
        }
    }
}
