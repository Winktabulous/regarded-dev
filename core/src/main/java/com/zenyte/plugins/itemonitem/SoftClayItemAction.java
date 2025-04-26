/**
 * 
 */
package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.enums.ContainerItem;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Noele | Jul 18, 2018 : 8:40:49 PM
 * @see https://noeles.life || noele@zenyte.com
 */
public class SoftClayItemAction implements ItemOnItemAction {
	
	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		if(from.getId() != 434 && to.getId() != 434) {
			player.sendMessage("Nothing interesting happens.");
			return;
		}
		
		ContainerItem water;
		if(ContainerItem.all.containsKey(from.getId()))
			water = ContainerItem.all.get(from.getId());
		else
			water = ContainerItem.all.get(to.getId());
		
		if(water == null)
			return;
		
		player.getInventory().replaceItem(water.getType().getEmpty().getId(), 1, player.getInventory().getContainer().getSlotOf(water.getContainer().getId()));
		player.getInventory().deleteItem(434, 1);
		player.getInventory().addItem(1761, 1);
	}


	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for(ContainerItem water : ContainerItem.lists.get("water"))
			list.add(water.getContainer().getId());

		list.add(434);
		return list.toArray(new int[list.size()]);
	}

}
