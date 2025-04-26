package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.followers.FishbowlD;
import com.zenyte.plugins.dialogue.followers.PetFishPlayD;

/**
 * @author Kris | 25. aug 2018 : 22:23:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Fishbowl extends ItemPlugin {

	@Override
	public void handle() {
		bind("Talk-At", (player, item, slotId) -> player.getDialogueManager().start(new FishbowlD(player, -1)));
		bind("Play-With", (player, item, slotId) -> player.getDialogueManager().start(new PetFishPlayD(player)));
		bind("Feed", (player, item, slotId) -> {
			if (!player.getInventory().containsItem(272, 1)) {
				player.sendMessage("You need some fish food to feed the fish.");
				return;
			}
			player.getInventory().deleteItem(272, 1);
			player.sendMessage("The fish seem to be happier after eating some of that food.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 6671 };
	}

}
