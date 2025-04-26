package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 25. aug 2018 : 18:46:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class HerbSack extends ItemPlugin {

	public static final Item HERB_SACK = new Item(13226);
	public static final Item HERB_SACK_OPEN = new Item(ItemId.OPEN_HERB_SACK);

	@Override
	public void handle() {
		bind("Fill", (player, item, slotId) -> player.getHerbSack().fill());
		bind("Check", (player, item, slotId) -> player.getHerbSack().check());
		bind("Empty", (player, item, slotId) -> player.getHerbSack().empty(player.getInventory().getContainer()));
		bind("Open", (player, item, slotId) -> {
			player.getHerbSack().setOpen(true);
			player.getInventory().set(slotId, HERB_SACK_OPEN);
			player.sendMessage("You open your herb sack, ready to fill it.");
		});
		bind("Close", (player, item, slotId) -> {
			player.getHerbSack().setOpen(false);
			player.getInventory().set(slotId, HERB_SACK);
			player.sendMessage("You close your herb sack.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { HERB_SACK.getId(), HERB_SACK_OPEN.getId() };
	}

}
