package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.actions.LeatherCrafting;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.skills.LeatherCraftingD;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:21.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class LeatherCraftingItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (LeatherCrafting.isMaterial(player, from, to)) {
			final Item[] items = LeatherCrafting.getCategory(player, from, to);
			final int category = player.getTemporaryAttributes().get("LeatherCategory") != null ? (int) player.getTemporaryAttributes().get("LeatherCategory") : -1;
			if (!player.getInventory().containsItem(CraftingDefinitions.THREAD) && category != 2 && category != 3 && category != 10) {
				player.getDialogueManager().start(new ItemChat(player, CraftingDefinitions.THREAD, "You need some thread to make anything out of " + CraftingDefinitions.MATERIALS[category][0].getDefinitions().getName().toLowerCase() + "."));
				return;
			}
			player.getDialogueManager().start(new LeatherCraftingD(player, items));
			return;
		}
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final Item[] items : CraftingDefinitions.MATERIALS) {
			for (final Item item : items) {
				list.add(item.getId());
			}
		}
		list.add(CraftingDefinitions.NEEDLE.getId());
		list.add(CraftingDefinitions.CHISEL.getId());
		list.add(2370);
		return list.toArray(new int[list.size()]);
	}
}
