package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

/**
 * The gem bag does not lose its contents, this is presumably due to it being able to carry up to 300 non-stacking
 * items, which would easily cause some overflows on death.
 * @author Kris | 12/06/2022
 */
class GemBagItemAction : ItemActionPlugin() {
	init {
		items(ItemId.GEM_BAG_12020, ItemId.OPEN_GEM_BAG)

		death {
			kept { yield(item) }
			status { ItemDeathStatus.KEEP_ON_DEATH }
		}
	}
}
