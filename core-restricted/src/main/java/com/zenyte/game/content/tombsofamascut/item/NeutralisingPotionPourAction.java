package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.content.tombsofamascut.encounter.ApmekenEncounter;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Savions.
 */
public class NeutralisingPotionPourAction extends ItemPlugin {

	@Override public void handle() {
		bind("Pour", (player, item, container, slotId) -> {
			if (player.getArea() instanceof final ApmekenEncounter encounter) {
				encounter.pourPotion(player);
			}
		});
	}

	@Override public int[] getItems() {
		return new int[] {ItemId.NEUTRALISING_POTION};
	}
}
