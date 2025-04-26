package com.near_reality.plugins.item

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue

class InfernoSkipScroll: ItemPlugin() {

	override fun handle() {
		bind("Activate") { p: Player, item: Item, _: Int ->
			if (p.getBooleanAttribute("used_inferno_skip_scroll")) {
				p.sendMessage("You already used the Inferno skip scroll.")
				return@bind
			}

			p.dialogue {
				item(item, "Redeeming this scroll will make your next Inferno challenge start from wave 67 and will consume the scroll.")
				options("This will consume the scroll!", Dialogue.DialogueOption("Consume.") {
					if (p.getBooleanAttribute("used_inferno_skip_scroll")) {
						p.sendMessage("You already used the Inferno skip scroll.")
						return@DialogueOption
					}

					p.inventory.deleteItem(item)
					p.putBooleanAttribute("used_inferno_skip_scroll", true)
					p.dialogue {
						item(item, "You read the scroll, next Inferno encounter will start from wave 67.")
					}
				}, Dialogue.DialogueOption("Cancel."))
			}
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(32160)
	}

}