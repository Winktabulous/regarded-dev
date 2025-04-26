package com.zenyte.plugins.item;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

import java.util.Map;

/**
 * @author Kris | 25. aug 2018 : 18:32:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class GemBag extends ItemPlugin {

	public static final Map<Integer, String> GEMS = ImmutableMap.<Integer, String>builder().put(1623, "Sapphires").put(1621, "Emeralds")
			.put(1619, "Rubies").put(1617, "Diamonds").put(1631, "Dragonstones").build();

	@Override
	public void handle() {
		bind("Fill", (player, item, slotId) -> player.getGemBag().fill());
		bind("Check", (player, item, slotId) -> player.getGemBag().check());
		bind("Empty", (player, item, slotId) -> player.getGemBag().empty(player.getInventory().getContainer()));
		bind("Open", (player, item, slotId) -> {
			player.getGemBag().setOpen(true);
			player.getInventory().set(slotId, com.zenyte.game.model.item.containers.GemBag.GEM_BAG_OPEN);
			player.sendMessage("You open your gem bag, ready to fill it.");
		});
		bind("Close", (player, item, slotId) -> {
			player.getGemBag().setOpen(false);
			player.getInventory().set(slotId, com.zenyte.game.model.item.containers.GemBag.GEM_BAG);
			player.sendMessage("You close your gem bag.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[]{com.zenyte.game.model.item.containers.GemBag.GEM_BAG.getId(), ItemId.OPEN_GEM_BAG};
	}

}
