package com.zenyte.plugins.item;

import com.zenyte.game.model.item.enums.ContainerItem;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 25. aug 2018 : 22:20:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Vessel extends ItemPlugin {
	@Override
	public void handle() {
		bind("Empty", (player, item, slotId) -> {
			final ContainerItem container = ContainerItem.all.get(item.getId());
			if (container == null) {
				return;
			}
			player.getInventory().replaceItem(container.getType().getEmpty().getId(), 1, slotId);
			player.sendMessage("You empty the contents of the " + container.getType().getEmpty().getName().toLowerCase() + " onto the floor.");
		});
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final ContainerItem container : ContainerItem.lists.get("empty")) {
			list.add(container.getContainer().getId());
		}
		return list.toArray(new int[0]);
	}
}
