package com.zenyte.game.content.boss.nightmare.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class EldritchNightmareStaffPlugin extends ItemPlugin implements PairedItemOnItemPlugin {

	private static final Item STAFF = new Item(ItemId.ELDRITCH_NIGHTMARE_STAFF);
	private static final Item[] COMPONENTS = {new Item(ItemId.NIGHTMARE_STAFF), new Item(ItemId.ELDRITCH_ORB)};

	@Override
	public void handle() {
		bind("Dismantle", (player, item, slotId) -> {
			if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("Not enough space in your inventory to dismantle the staff.");
				return;
			}

			player.getInventory().deleteItem(item);
			player.getInventory().addItems(COMPONENTS);
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					doubleItem(COMPONENTS[0], COMPONENTS[1], "You remove the orb from the staff.");
				}
			});
		});
	}

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		player.getInventory().deleteItemsIfContains(COMPONENTS, () -> {
			player.getInventory().addItem(STAFF);
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					item(STAFF, "You add the orb to the staff.");
				}
			});
		});
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.ELDRITCH_NIGHTMARE_STAFF};
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[]{new ItemPair(ItemId.NIGHTMARE_STAFF, ItemId.ELDRITCH_ORB)};
	}

}
