package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
/**
 * @author Savions.
 */
public class SuppliesBagAction extends ItemPlugin {

	@Override public void handle() {
		bind("Open", (player, item, container, slotId) -> GameInterface.TOA_SUPPLIES_INV.open(player));
		bind("Withdraw 1", (player, item, container, slotId) -> player.getTOAManager().withdrawSupplies(1));
		bind("Withdraw All", (player, item, container, slotId) -> player.getTOAManager().withdrawSupplies(28));
		bind("Resupply", (player, item, container, slotId) -> player.getTOAManager().reSupply());
	}

	@Override public int[] getItems() {
		return new int[] {ItemId.SUPPLIES};
	}
}