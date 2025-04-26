package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 25. aug 2018 : 22:32:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class LightBoxItem extends ItemPlugin {

	@Override
	public void handle() {
		bind("Open", (player, item, slotId) -> {
			if (item.getCharges() == 2) {
				player.sendMessage("You've already solved this light box.");
				return;
			}
			player.getLightBox().open(item.getCharges() == 0);
			if (item.getCharges() == 0) {
				item.setCharges(1);
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 20355 };
	}

}
