package com.near_reality.plugins.item.actions.death_items

import com.near_reality.plugins.item.actions.ItemActionPlugin
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.item.ItemId.*

/**
 * @author Kris | 12/06/2022
 */
class RunePouchItemAction : ItemActionPlugin() {
	init {
		items(RUNE_POUCH, RUNE_POUCH_L, DIVINE_RUNE_POUCH, DIVINE_RUNE_POUCH_L)

		death {
			kept {
				yield(item)
				val runePouch = player.runePouch
				for (rune in runePouch.container.items.values) {
					if (rune == null) continue
					yield(rune)
				}
			}
			status {
				ItemDeathStatus.KEEP_ON_DEATH
			}
		}
	}
}