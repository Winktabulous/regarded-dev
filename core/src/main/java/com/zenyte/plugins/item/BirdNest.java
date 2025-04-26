package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.woodcutting.actions.BirdNests;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Corey
 * @since 18/11/18
 */
public class BirdNest extends ItemPlugin {
	@Override
	public void handle() {
		bind("Search", (player, item, slotId) -> {
			if (player.getInventory().getFreeSlots() < 1) {
				player.sendMessage("You need some more inventory space to search the nest.");
				return;
			}
			final BirdNests.Nests nest = BirdNests.Nests.LOOTABLE_NESTS.get(item.getId());
			final Item loot = nest.rollLoot();
			if (loot == null) {
				return;
			}
			player.getInventory().addOrDrop(loot);
			player.getInventory().replaceItem(BirdNests.Nests.EMPTY.getNestItemId(), 1, slotId);
		});
	}

	@Override
	public int[] getItems() {
		return BirdNests.Nests.LOOTABLE_NESTS.keySet().toIntArray();
	}
}
