package com.zenyte.plugins.itemonobject;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10. nov 2017 : 23:18.55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class JewelleryFurnaceAction implements ItemOnObjectAction {

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		if (item.getId() == CraftingDefinitions.SILVER_BAR.getId()) {
			GameInterface.SILVER_JEWELLERY_INTERFACE.open(player);
		} else if (item.getId() == CraftingDefinitions.GOLD_BAR.getId()) {
			GameInterface.GOLD_JEWELLERY_INTERFACE.open(player);
		}
	}

	@Override
	public Object[] getItems() {
		return new Object[] { 2355, 2357 };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Furnace", "Clay forge", "Lava forge" };
	}

}
