package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class KeptItemsItemAction : ItemActionPlugin() {
    init {
        items(
            OLD_SCHOOL_BOND_UNTRADEABLE,
            _50_DONATOR_SCROLL,
            32149, 32150, 32151, 32152, 32153, 32154, 32155, 32156,
            SALVE_AMULET, SALVE_AMULET_E, SALVE_AMULETI, SALVE_AMULETEI,
            SALVE_AMULETI_25250, SALVE_AMULETEI_25278, SALVE_AMULETI_26763, SALVE_AMULETEI_26782
        )

        death {
            setAlwaysKeptOnDeath()
            kept { yield(item) }
            status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}