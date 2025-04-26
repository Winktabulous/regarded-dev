package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.content.tombsofamascut.encounter.ApmekenEncounter;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnPlayerPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class NeutralisingPotionOnPlayerAction implements ItemOnPlayerPlugin {

	@Override public void handleItemOnPlayerAction(Player player, Item item, int slot, Player target) {
		if (player.getArea() instanceof final ApmekenEncounter encounter) {
			encounter.usePotionOn(player, target);
		}
	}

	@Override public int[] getItems() {
		return new int[] {ItemId.NEUTRALISING_POTION};
	}
}
