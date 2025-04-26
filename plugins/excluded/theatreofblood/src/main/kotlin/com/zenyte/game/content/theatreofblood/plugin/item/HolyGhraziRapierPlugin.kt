package com.zenyte.game.content.theatreofblood.plugin.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player

class HolyGhraziRapierPlugin : ItemPlugin() {

	override fun handle() {
		bind(
			"Dismantle"
		) { player: Player, item: Item, slotId: Int ->
			if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(ItemId.HOLY_ORNAMENT_KIT, ItemId.GHRAZI_RAPIER)) {
				player.inventory.deleteItem(item)
				player.inventory.addItem(Item(ItemId.HOLY_ORNAMENT_KIT))
				player.inventory.addItem(Item(ItemId.GHRAZI_RAPIER))
				player.sendMessage("You dismantle your rapier.")
			} else {
				player.sendMessage("Not enough space in your inventory.")
			}
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(
			ItemId.HOLY_GHRAZI_RAPIER,
		)
	}

}