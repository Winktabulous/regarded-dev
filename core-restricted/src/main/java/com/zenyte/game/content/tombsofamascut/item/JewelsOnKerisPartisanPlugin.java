package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;

@SuppressWarnings("unused")
public class JewelsOnKerisPartisanPlugin implements PairedItemOnItemPlugin {

	public static final Item KERIS_PARTISAN = new Item(ItemId.KERIS_PARTISAN);

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final int index = getMatchingPairIndex(from, to, JewelData.pairs);
		final JewelData data = JewelData.values[index];
		attach(player, data);
	}

	public static void attach(Player player, JewelData data) {
		if (!player.getInventory().containsItem(data.item)) {
			return;
		}

		if (!player.getInventory().containsItem(KERIS_PARTISAN)) {
			player.sendMessage("You do not have a keris partisan to attach this jewel into.");
			return;
		}

		player.getInventory().deleteItem(data.item);
		player.getInventory().deleteItem(KERIS_PARTISAN);
		player.getInventory().addItem(data.attached);
		player.sendMessage("You attach the " + ItemDefinitions.nameOf(data.item.getId()).toLowerCase() + " to your keris partisan.");
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return JewelData.pairs;
	}

	public enum JewelData {
		RED(ItemId.EYE_OF_THE_CORRUPTOR, ItemId.KERIS_PARTISAN_OF_CORRUPTION, "A jewel which can be attached to a Keris Partisan granting it a special attack which when used increases damage taken by creatures within Tombs of Amascut."),
		BLUE(ItemId.BREACH_OF_THE_SCARAB, ItemId.KERIS_PARTISAN_OF_BREACHING, "A jewel which can be attached to a Keris Partisan granting it increased accuracy against Kalphites and Scarabs."),
		YELLOW(ItemId.JEWEL_OF_THE_SUN, ItemId.KERIS_PARTISAN_OF_THE_SUN, "A jewel which can be attached to a Keris Partisan granting it a passive ability to restore health at the cost of prayer when defeating creatures within Tombs of Amascut and to have increased accuracy against them when they are low in hitpoints.");

		public static final JewelData[] values = values();
		public static final ItemPair[] pairs;
		private final Item item, attached;
		private final String inspectMessage;

		JewelData(final int id, final int attached, final String inspectMessage) {
			this.item = new Item(id);
			this.attached = new Item(attached);
			this.inspectMessage = inspectMessage;
		}

		public Item getItem() {
			return item;
		}

		public Item getAttached() {
			return attached;
		}

		public String getInspectMessage() {
			return inspectMessage;
		}

		static {
			final int len = values.length;
			pairs = new ItemPair[len];
			for (int i = 0; i < len; i++) {
				pairs[i] = new ItemPair(ItemId.KERIS_PARTISAN, values[i].item.getId());
			}
		}

	}

}
