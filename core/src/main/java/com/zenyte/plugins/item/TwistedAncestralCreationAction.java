package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

public final class TwistedAncestralCreationAction implements ItemOnItemAction {

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final Item kit = from.getId() == ItemId.TWISTED_ANCESTRAL_COLOUR_KIT ? from : to;
		final Item armor = kit == from ? to : from;

		final Inventory inventory = player.getInventory();
		inventory.deleteItem(fromSlot, from);
		inventory.deleteItem(toSlot, to);
		switch (armor.getId()) {
			case ItemId.ANCESTRAL_HAT -> inventory.addItem(new Item(ItemId.TWISTED_ANCESTRAL_HAT));
			case ItemId.ANCESTRAL_ROBE_TOP -> inventory.addItem(new Item(ItemId.TWISTED_ANCESTRAL_ROBE_TOP));
			case ItemId.ANCESTRAL_ROBE_BOTTOM -> inventory.addItem(new Item(ItemId.TWISTED_ANCESTRAL_ROBE_BOTTOM));
		}
		player.sendMessage("The ornament kit attaches itself to the item.");
	}

	@Override
	public int[] getItems() {
		return null;
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[]{
				new ItemPair(ItemId.ANCESTRAL_HAT, ItemId.TWISTED_ANCESTRAL_COLOUR_KIT),
				new ItemPair(ItemId.ANCESTRAL_ROBE_TOP, ItemId.TWISTED_ANCESTRAL_COLOUR_KIT),
				new ItemPair(ItemId.ANCESTRAL_ROBE_BOTTOM, ItemId.TWISTED_ANCESTRAL_COLOUR_KIT)
		};
	}
}
