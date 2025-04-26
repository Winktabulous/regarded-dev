package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class SeedBoxItemAction : ItemActionPlugin() {
	init {
		items(SEED_BOX, OPEN_SEED_BOX)

		death {
			kept {
				yield(item)
			}
			status { ItemDeathStatus.KEEP_ON_DEATH }
		}
	}
}