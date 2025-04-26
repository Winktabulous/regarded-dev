package com.near_reality.plugins.item

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class BowOfFaerdhinenCItemOption : ItemPlugin() {

	override fun handle() {
		bind("Uncharge") { player: Player, item: Item, slot: Int ->
			player.dialogue {
				plain("When you uncharge this you will receive the bow but ${Colour.RED.wrap("no crystal shards")}.")
				options("Are you sure you want to dismantle this?") {
					"Yes, dismantle it." {
						player.inventory.run {
							if (deleteItem(slot, item).result == RequestResult.SUCCESS) {
								addItems(Item(CrystalWeapon.BowOfFaerdhinen.inactiveId))
								player.sendMessage("You revert the ${item.name} back into its raw materials.")
							}
						}
					}
					"No, I want to keep it" {}
				}
			}
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(
			ItemId.BOW_OF_FAERDHINEN_C_25884,
			ItemId.BOW_OF_FAERDHINEN_C_25886,
			ItemId.BOW_OF_FAERDHINEN_C_25888,
			ItemId.BOW_OF_FAERDHINEN_C_25890,
			ItemId.BOW_OF_FAERDHINEN_C_25892,
			ItemId.BOW_OF_FAERDHINEN_C_25894,
			ItemId.BOW_OF_FAERDHINEN_C_25896,
		)
	}

}