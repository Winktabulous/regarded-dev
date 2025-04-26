package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;

public class FossilsOnAncientWyvernShieldAction implements PairedItemOnItemPlugin {

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final Item numulite = from.getId() == ItemId.NUMULITE ? from : to;
		final Item shield = numulite == from ? to : from;
		if (shield.getCharges() == 50) {
			player.sendMessage("Your shield is already fully charged.");
			return;
		}

		player.sendInputInt("How many charges? (100 per charge)", amount -> {
			int availableCharges = player.getInventory().getAmountOf(ItemId.NUMULITE) / 100;
			if (availableCharges == 0) {
				player.sendMessage("You don't have enough Numulites to charge your shield.");
				return;
			}

			int chargesToCharge = Math.min(amount, availableCharges);
			chargesToCharge = Math.min(50 - shield.getCharges(), chargesToCharge);
			if (chargesToCharge == 0) {
				player.sendMessage("You were unable to charge your shield.");
				return;
			}

			shield.setCharges(Math.max(0, shield.getCharges() + chargesToCharge));
			player.getInventory().deleteItem(new Item(ItemId.NUMULITE, chargesToCharge * 100));
			if (shield.getId() == ItemId.ANCIENT_WYVERN_SHIELD_21634) {
				shield.setId(ItemId.ANCIENT_WYVERN_SHIELD);
			}

			player.getInventory().refreshAll();
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {
				new ItemPair(ItemId.ANCIENT_WYVERN_SHIELD, ItemId.NUMULITE),
				new ItemPair(ItemId.ANCIENT_WYVERN_SHIELD_21634, ItemId.NUMULITE),
		};
	}

}
