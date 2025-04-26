package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

class NeitiznotFaceguardItemAction : ItemActionPlugin() {
	init {
		items(NEITIZNOT_FACEGUARD)

		death {
			if (pvp) {
				lost {
					yield(Item(HELM_OF_NEITIZNOT))
					yield(Item(BASILISK_JAW))
				}
				status { ItemDeathStatus.DROP_ON_DEATH }
			} else {
				lost { yield(item) }
				status { ItemDeathStatus.GO_TO_GRAVESTONE }
			}
		}
	}
}
