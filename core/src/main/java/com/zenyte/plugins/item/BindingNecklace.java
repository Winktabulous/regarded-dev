package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 27. aug 2018 : 14:36:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BindingNecklace extends ItemPlugin {
	@Override
	public void handle() {
		bind("Check", (player, item, container, slotId) -> {
			final int uses = player.getNumericAttribute("binding necklace uses").intValue();
			player.sendMessage("You have " + (16 - uses) + " charges left before your Binding necklace disintegrates.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {5521};
	}
}
