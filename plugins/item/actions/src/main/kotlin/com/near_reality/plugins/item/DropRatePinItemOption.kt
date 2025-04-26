package com.near_reality.plugins.item

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.model.ui.testinterfaces.GameNoticeboardInterface
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue

class DropRatePinItemOption: ItemPlugin() {

	override fun handle() {
		bind("Claim") { p: Player, item: Item, _: Int ->
			if (p.getBooleanAttribute("drop_rate_pin_claimed")) {
				p.sendMessage("You already have the drop rate boost activated.")
				return@bind
			}

			p.dialogue {
				item(item, "Claiming this pin will increase your drop rate by 5%.")
				options("This will consume the pin!", Dialogue.DialogueOption("Claim.") {
					if (p.getBooleanAttribute("drop_rate_pin_claimed")) {
						p.sendMessage("You already have the drop rate boost activated.")
						return@DialogueOption
					}

					p.inventory.deleteItem(item)
					GameNoticeboardInterface.updateDropRate(p)
					p.putBooleanAttribute("drop_rate_pin_claimed", true)
					p.dialogue {
						item(item, "You claim the pin, your drop rate has been increased by 5%.")
					}
				}, Dialogue.DialogueOption("Cancel."))
			}
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(32201)
	}

}