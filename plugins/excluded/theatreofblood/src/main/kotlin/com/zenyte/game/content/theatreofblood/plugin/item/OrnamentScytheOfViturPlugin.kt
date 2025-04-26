package com.zenyte.game.content.theatreofblood.plugin.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue

@Suppress("unused")
class OrnamentScytheOfViturPlugin : ItemPlugin() {

	override fun handle() {
		bind(
			"Wield"
		) { player: Player, item: Item, slotId: Int ->
			if (player.temporaryAttributes.getOrDefault("TOB_inside", false) as Boolean) {
				player.temporaryAttributes["TOB_scythe_equipped"] = true
			}
			player.equipment.wear(slotId)
		}
		bind(
			"Charge"
		) { player: Player, item: Item, slotId: Int ->
			player.sendMessage(
				"Your scythe must be charged using a special " + "vyre well found at Ver Sinhaza."
			)
		}
		bind(
			"Dismantle"
		) { player: Player, item: Item, slotId: Int ->
			for (value in HolyOrnamentKit.OrnamentData.VALUES) {
				if (value.product == item.id) {
					if (player.inventory.hasSpaceFor(value.ornament, value.base)) {
						player.inventory.deleteItem(item)
						player.inventory.addItem(Item(value.ornament))
						player.inventory.addItem(Item(value.base))
						player.sendMessage("You dismantle your scythe.")
					} else {
						player.sendMessage("Not enough space in your inventory.")
					}
				}
			}
		}
		bind("Uncharge") { player: Player, item: Item, slotId: Int ->
			if (player.duel != null && player.duel.inDuel()) {
				player.sendMessage("You can't do this during a duel.")
				return@bind
			}
			player.dialogueManager.start(object : Dialogue(player) {
				override fun buildDialogue() {
					plain("You will not be able to re-obtain your vials of blood and blood runes.")
					options(
						"Uncharge the scythe?<br><col=ff0000>Alternatively you can unload the charges back into the well.",
						DialogueOption(
							"Yes."
						) {
							//Verify the existence of the item.
							if (player.inventory.getItem(slotId) === item) {
								item.charges = 0
								when (item.id) {
									ItemId.HOLY_SCYTHE_OF_VITUR -> item.id =
										ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED

									ItemId.SANGUINE_SCYTHE_OF_VITUR -> item.id =
										ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED
								}
								player.inventory.refresh(slotId)
								player.sendMessage("You uncharge your scythe.")
							}
						},
						DialogueOption("No.")
					)
				}
			})
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(
			ItemId.HOLY_SCYTHE_OF_VITUR,
			ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED,
			ItemId.SANGUINE_SCYTHE_OF_VITUR,
			ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED
		)
	}

}