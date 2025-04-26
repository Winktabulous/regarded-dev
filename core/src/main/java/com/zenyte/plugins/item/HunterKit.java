package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 25. aug 2018 : 22:28:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class HunterKit extends ItemPlugin {

	private final int[] rewards = { 10150, 10010, 10029, 596, 10006, 11260, 10008, 10031 };

	@Override
	public void handle() {
		bind("Open", (player, item, slotId) -> {
			if (player.getInventory().getFreeSlots() >= 7) {
				player.getInventory().deleteItem(11159, 1);
				for (final int reward : rewards) {
					player.getInventory().addItem(reward, 1);
				}
				player.sendMessage("You open the hunter kit and get a cache of hunter supplies.");
			} else {
				player.getDialogueManager().start(new PlainChat(player, "You need at least 7 free inventory spaces to open this!"));
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 11159 };
	}

}
