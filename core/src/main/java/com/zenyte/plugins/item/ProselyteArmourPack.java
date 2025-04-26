package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 26. aug 2018 : 20:21:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ProselyteArmourPack extends ItemPlugin {
	private static final Item[] PROSELYTE_HARNESS_M = new Item[] {new Item(9672), new Item(9674), new Item(9676)};
	private static final Item[] PROSELYTE_HARNESS_F = new Item[] {new Item(9672), new Item(9674), new Item(9678)};

	@Override
	public void handle() {
		bind("Unpack", (player, item, slotId) -> {
			final Inventory inventory = player.getInventory();
			if (inventory.getFreeSlots() < 2) {
				player.sendMessage("You need at least 3 free inventory slots to unpack the set.");
				return;
			}
			inventory.deleteItem(item);
			for (final Item piece : (item.getName().equals("Proselyte harness m") ? PROSELYTE_HARNESS_M : PROSELYTE_HARNESS_F)) {
				inventory.addItem(piece);
			}
			player.sendMessage("You unpack the proselyte armour set.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {9666, 9670};
	}
}
