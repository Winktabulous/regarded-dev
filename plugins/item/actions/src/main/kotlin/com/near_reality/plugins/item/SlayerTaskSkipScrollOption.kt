package com.near_reality.plugins.item

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue

class SlayerTaskSkipScrollOption : ItemPlugin() {

	override fun handle() {
		bind("Redeem") { p: Player, item: Item, _: Int ->
			if (p.slayer.assignment == null) {
				p.sendMessage("You don't have an assignment to skip.")
				return@bind
			}

			p.dialogue {
				item(item, "Redeeming this scroll will reset your current Slayer task and will consume the scroll.")
				options("This will consume the scroll!", Dialogue.DialogueOption("Consume.") {
					if (p.slayer.assignment == null) {
						p.sendMessage("You don't have an assignment to skip.")
						return@DialogueOption
					}

					p.slayer.removeTask()
					p.inventory.deleteItem(item.copy(1))
					p.dialogue {
						item(item, "Your Slayer assignment has been cancelled.")
					}
				}, Dialogue.DialogueOption("Cancel."))
			}
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(32158)
	}

}