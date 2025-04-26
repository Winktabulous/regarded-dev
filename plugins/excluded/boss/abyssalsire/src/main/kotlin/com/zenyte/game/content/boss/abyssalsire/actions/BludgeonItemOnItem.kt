package com.zenyte.game.content.boss.abyssalsire.actions

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue

class BludgeonItemOnItem : ItemOnItemAction {

	override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
		if (!player.inventory.containsAll(bludgeonItems)) {
			player.dialogue { item(abyssalBludgeon, "You need Bludgeon Axon, Bludgeon Claw and Bludgeon Spine to combine into an Abyssal Bludgeon.") }
			return
		}

		player.inventory.deleteItems(bludgeonAxon, bludgeonClaw, bludgeonSpine)
		player.inventory.addItem(abyssalBludgeon)
		player.dialogue { item(abyssalBludgeon, "You combine Bludgeon Axon, Bludgeon Claw and Bludgeon Spine into an Abyssal Bludgeon.") }
	}

	override fun getItems(): IntArray? {
		return null
	}

	override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
		return arrayOf(
			ItemOnItemAction.ItemPair(ItemId.BLUDGEON_AXON, ItemId.BLUDGEON_CLAW),
			ItemOnItemAction.ItemPair(ItemId.BLUDGEON_AXON, ItemId.BLUDGEON_SPINE),
			ItemOnItemAction.ItemPair(ItemId.BLUDGEON_CLAW, ItemId.BLUDGEON_SPINE),
			ItemOnItemAction.ItemPair(ItemId.BLUDGEON_CLAW, ItemId.BLUDGEON_AXON),
			ItemOnItemAction.ItemPair(ItemId.BLUDGEON_SPINE, ItemId.BLUDGEON_CLAW),
			ItemOnItemAction.ItemPair(ItemId.BLUDGEON_SPINE, ItemId.BLUDGEON_AXON),
		)
	}

	companion object {
		private val bludgeonAxon = Item(ItemId.BLUDGEON_AXON)
		private val bludgeonClaw = Item(ItemId.BLUDGEON_CLAW)
		private val bludgeonSpine = Item(ItemId.BLUDGEON_SPINE)
		private val abyssalBludgeon = Item(ItemId.ABYSSAL_BLUDGEON)
		private val bludgeonItems = mutableListOf(ItemId.BLUDGEON_AXON, ItemId.BLUDGEON_CLAW, ItemId.BLUDGEON_SPINE)
	}

}