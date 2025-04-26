package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 3 feb. 2018 : 18:30:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class DecantingItemAction implements ItemOnItemAction {

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		//Potions.decantPotion(player, from, to, fromSlot, toSlot);
	}

	@Override
	public int[] getItems() {
		/*final IntArrayList list = new IntArrayList();
		for (final Potion potion : Potion.VALUES) {
			for (int i = 0; i < potion.getId().length; i++) {
				list.add(potion.getId()[i]);
			}
		}
		list.add(Potions.VIAL);
		return list.toArray(new int[list.size()]);*/
		return new int[0];
	}
	
	@Override
	public boolean includeEquivalentItems() {
		return true;
	}

}
