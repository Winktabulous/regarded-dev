package com.near_reality.plugins.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player

class SpicyStewOption: ItemPlugin() {

	override fun handle() {
		bind("Eat") { p: Player, _: Item, slot: Int ->
			if (p.variables.enhancedStewTick > 0) {
				p.sendMessage("You feel full from last stew.")
				return@bind
			}

			p.animation = Animation(829)
			p.inventory.replaceItem(ItemId.BOWL, 1, slot)
			p.variables.enhancedStewTick = 200
			p.sendMessage("You eat the stew and feel energized.")
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(32159)
	}

}