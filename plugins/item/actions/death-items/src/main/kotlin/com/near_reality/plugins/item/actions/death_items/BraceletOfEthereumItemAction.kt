package com.near_reality.plugins.item.actions.death_items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.BRACELET_OF_ETHEREUM
import com.zenyte.game.item.ItemId.BRACELET_OF_ETHEREUM_UNCHARGED
import com.zenyte.game.item.ItemId.REVENANT_ETHER
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * @author Kris | 12/06/2022
 */
class BraceletOfEthereumItemAction : ItemActionScript() {
    init {
        items(BRACELET_OF_ETHEREUM, BRACELET_OF_ETHEREUM_UNCHARGED)

        death {
            setAlwaysLostOnDeath()
            lost {
                yield(Item(BRACELET_OF_ETHEREUM_UNCHARGED))
                if (item.charges > 0) {
                    yield(Item(REVENANT_ETHER, item.charges))
                }
            }
            status { if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE }
        }
    }
}
