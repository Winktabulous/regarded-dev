package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.containers.HerbSack;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Tommeh | 14 jul. 2018 | 21:57:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class HerbOnHerbSackItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final Item herb = from.getId() != 13226 ? from : to;
		final int herbSlot = from.getId() != 13226 ? fromSlot : toSlot;
		final int size = player.getHerbSack().getSize();
		if (size == 420) {
			player.sendMessage("You cannot more than 420 herbs in your herb sack.");
			return;
		}
		if (player.getHerbSack().getAmountOf(herb.getId()) == 30) {
			player.sendMessage("You cannot store anymore " + herb.getName().toLowerCase() + "s.");
			return;
		}
		player.getHerbSack().getContainer().deposit(player, player.getInventory().getContainer(), herbSlot, 1);
		player.getInventory().refreshAll();
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final int id : HerbSack.HERBS) {
			list.add(id);
		}
		list.add(13226);
		return list.toArray(new int[list.size()]);
	}
}
