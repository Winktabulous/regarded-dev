package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * @author Stan van der Bend
 */
class ImbuedCapesItemAction : ItemActionPlugin() {
    init {
        items(
            ItemId.IMBUED_ANCIENT_CAPE,
            ItemId.IMBUED_ARMADYL_CAPE,
            ItemId.IMBUED_BANDOS_CAPE,
            ItemId.IMBUED_SEREN_CAPE
        )

        death {
            setAlwaysKeptOnDeath()
            kept { yield(item) }
            status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}
