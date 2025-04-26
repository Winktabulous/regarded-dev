package com.zenyte.game.content.boss.abyssalsire.actions

import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusArea
import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusCorner
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.GlobalAreaManager

class UnsiredOnFontOfConsumption : ItemOnObjectAction {

	override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject) {
		if (!player.inventory.containsItem(item)) {
			return
		}

		val lair = GlobalAreaManager[AbyssalNexusCorner.NORTH_EAST.areaName] as AbyssalNexusArea
		val size = lair.players.size
		if (size == 0) {
			player.dialogue { plain("There are no adventurers in this chamber.") }
		} else if (size == 1) {
			player.dialogue { plain("There is 1 adventurer in this chamber.") }
		} else {
			player.dialogue { plain("There are $size adventurers in this chamber.") }
		}

		player.lock(4)
		player.dialogueManager.start(object : Dialogue(player) {
			override fun buildDialogue() {
				item(Item(ItemId.UNSIRED), "You place the Unsired into the Font of Consumption...", false)
			}
		})
		player.delay(2) {
			player.animation = Animation.LADDER_DOWN
			World.sendGraphics(
				SPLASH_GFX,
				player.location.transform(0, 1)
			)
			player.delay(0) {
				player.dialogueManager.start(object : Dialogue(player) {
					override fun buildDialogue() {
						val reward = rollItem(player)
						item(reward, "The Font consumes the Unsired and returns you a reward.")
						player.inventory.deleteItem(item)
						player.inventory.addOrDrop(reward)
						player.collectionLog.add(reward)
					}
				})
			}
		}
	}

	private fun rollItem(player: Player): Item {
		val roll = Utils.randomNoPlus(128)
		if (roll < 5) {
			return Item(ItemId.ABYSSAL_ORPHAN)
		} else if (roll < 15) {
			return Item(ItemId.ABYSSAL_HEAD)
		} else if (roll < 28) {
			return Item(ItemId.JAR_OF_MIASMA)
		} else if (roll < 40) {
			return Item(ItemId.ABYSSAL_WHIP)
		} else if (roll < 66) {
			return Item(ItemId.ABYSSAL_DAGGER)
		} else {
			val containsClaw = player.containsItem(ItemId.BLUDGEON_CLAW)
			return if (containsClaw && !player.containsItem(ItemId.BLUDGEON_SPINE)) {
				Item(ItemId.BLUDGEON_SPINE)
			} else if (containsClaw && !player.containsItem(ItemId.BLUDGEON_AXON)) {
				Item(ItemId.BLUDGEON_AXON)
			} else {
				Item(ItemId.BLUDGEON_CLAW)
			}
		}
	}

	override fun getItems(): Array<Any> {
		return arrayOf(ItemId.UNSIRED)
	}

	override fun getObjects(): Array<Any> {
		return arrayOf(ObjectId.THE_FONT_OF_CONSUMPTION)
	}

	companion object {
		private val SPLASH_GFX = Graphics(1276)
	}
}
