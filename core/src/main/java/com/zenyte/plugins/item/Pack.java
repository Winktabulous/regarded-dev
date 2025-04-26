package com.zenyte.plugins.item;

import com.zenyte.game.model.item.enums.ItemPack;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 25. aug 2018 : 22:34:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Pack extends ItemPlugin {
	@Override
	public void handle() {
		bind("Open", (player, item, slotId) -> {
			final ItemPack pack = ItemPack.DATA.get(item.getId());
			player.getInventory().deleteItem(slotId, item);
			if (!player.getInventory().checkSpace()) {
				return;
			}
			player.getInventory().addItem(pack.getItem());
			player.sendFilteredMessage("You open the " + item.getName().toLowerCase() + ".");
		});
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final ItemPack pack : ItemPack.VALUES) {
			list.add(pack.getPack().getId());
		}
		return list.toArray(new int[list.size()]);
	}
}
