package com.near_reality.plugins.item.actions.death_items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25884
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25886
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25888
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25890
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25892
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25894
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25896
import com.zenyte.game.item.ItemId.ENHANCED_CRYSTAL_WEAPON_SEED
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus

class BowfaItemAction : ItemActionScript() {
	init {
		items(
			BOW_OF_FAERDHINEN,
			BOW_OF_FAERDHINEN_C,
			BOW_OF_FAERDHINEN_C_25884,
			BOW_OF_FAERDHINEN_C_25886,
			BOW_OF_FAERDHINEN_C_25888,
			BOW_OF_FAERDHINEN_C_25890,
			BOW_OF_FAERDHINEN_C_25892,
			BOW_OF_FAERDHINEN_C_25894,
			BOW_OF_FAERDHINEN_C_25896
		)

		death {
			if (pvp) {
				lost {
					yield(Item(ENHANCED_CRYSTAL_WEAPON_SEED))
				}
				status { ItemDeathStatus.DROP_ON_DEATH }
			} else {
				lost { yield(item) }
				status { ItemDeathStatus.GO_TO_GRAVESTONE }
			}
		}
	}
}
