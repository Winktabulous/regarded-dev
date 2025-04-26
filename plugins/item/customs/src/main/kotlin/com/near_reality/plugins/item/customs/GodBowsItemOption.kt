package com.near_reality.plugins.item.customs

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon
import com.near_reality.game.content.custom.GodBow
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class GodBowsItemOption : ItemPlugin() {

	override fun handle() {
		bind("Uncharge") { player: Player, item: Item, slot: Int ->
			val godBow = GodBow.all.find { it.itemId == item.id } ?: return@bind

			player.dialogue {
				plain("When you dismantle this you will receive soul crystal and the bow but ${Colour.RED.wrap("no crystal shards")}.")
				options("Are you sure you want to dismantle this?") {
					"Yes, dismantle it." {
						player.inventory.run {
							val requiredSpace = 2
							if (checkSpace(requiredSpace - 1)) { // - 1 because we also delete an item!
								if (deleteItem(slot, item).result == RequestResult.SUCCESS) {
									addItems(Item(godBow.soulCrystalItemId), Item(CrystalWeapon.BowOfFaerdhinen.inactiveId))
									player.sendMessage("You revert the ${item.name} back into its raw materials.")
								}
							} else
								player.dialogue { plain("You do not have enough space in your inventory to do this!") }
						}
					}
					"No, I want to keep it" {}
				}
			}
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(
			CustomItemId.ARMADYL_BOW,
			CustomItemId.BANDOS_BOW,
			CustomItemId.SARADOMIN_BOW,
			CustomItemId.ZAMORAK_BOW
		)
	}

}