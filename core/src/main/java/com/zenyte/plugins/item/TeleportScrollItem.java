package com.zenyte.plugins.item;

import com.zenyte.game.content.itemtransportation.masterscrolls.TeleportScroll;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 26. aug 2018 : 16:40:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class TeleportScrollItem extends ItemPlugin {
	@Override
	public void handle() {
		bind("Teleport", (player, item, slotId) -> {
			final TeleportScroll scroll = TeleportScroll.getTeleportScroll(item);
			if (scroll == null) {
				return;
			}
			scroll.teleport(player);
		});
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		list.addAll(TeleportScroll.scrolls.keySet());
		return list.toArray(new int[list.size()]);
	}
}
