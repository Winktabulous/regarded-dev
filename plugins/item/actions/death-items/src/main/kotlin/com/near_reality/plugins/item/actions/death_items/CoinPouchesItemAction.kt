package com.near_reality.plugins.item.actions.death_items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.content.skills.thieving.CoinPouch
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * @author Kris | 12/06/2022
 */
class CoinPouchesItemAction : ItemActionScript() {
    init {
        items(CoinPouch.ITEMS.keys)

        death {
            val pouch = CoinPouch.ITEMS[item.id]
            lost { yield(Item(ItemId.COINS_995, com.zenyte.plugins.item.CoinPouch.getCoinAmount(pouch, item.amount))) }
            status { if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE }
        }
    }
}