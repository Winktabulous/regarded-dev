package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.prayer.actions.Bones;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.List;


public class DHideOnDHidePouch implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final int hideSlot = (from.getId() != 26306 && from.getId() != 26304) ? fromSlot : toSlot;
		final int size = player.getDragonhidePouch().getSize();
		if (size == 100) {
			player.sendMessage("You cannot more than 100 hides in your dragonhide pouch.");
			return;
		}
		player.getDragonhidePouch().getContainer().deposit(player, player.getInventory().getContainer(), hideSlot, 1);
		player.getInventory().refreshAll();
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		list.addAll(List.of(1747, 1749, 1751, 1753));
		list.add(26302);
		list.add(26300);
		return list.toArray(new int[list.size()]);
	}
}
