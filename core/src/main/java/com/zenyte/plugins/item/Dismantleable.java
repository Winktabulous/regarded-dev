package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.enums.DismantleableItem;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 25. aug 2018 : 22:30:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("unused")
public class Dismantleable extends ItemPlugin {

	@Override
	public void handle() {
		bind("Dismantle", Dismantleable::dismantle);
		bind("Dismantle kit", Dismantleable::dismantle);
	}

	public static void dismantle(final Player player, final Item item, final int slotId) {
		if (!player.getInventory().hasFreeSlots()) {
			player.sendMessage("You need some free inventory space to dismantle this item.");
			return;
		}
		final DismantleableItem dis = DismantleableItem.MAPPED_VALUES.get(item.getId());
		if (dis == null) {
			return;
		}
		player.getInventory().deleteItem(slotId, item);
		player.getInventory().addItem(dis.getKit(), 1);
		player.getInventory().addItem(dis.getBaseItem(), 1, item.getCharges());
		player.sendMessage("You dismantle the " + dis.getKitName() + " from the " + dis.getBaseName() + ".");
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final DismantleableItem val : DismantleableItem.VALUES) {
			final int completeItem = val.getCompleteItem();
			if (completeItem != 26484 && completeItem != 26520) {
				list.add(completeItem);
			}
		}
		return list.toArray(new int[0]);
	}
}
