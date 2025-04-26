package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.fletching.FletchingDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.skills.LogsFletchingD;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:25.21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class LogFletchingItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (FletchingDefinitions.isMaterial(from, to)) {
			player.getDialogueManager().start(new LogsFletchingD(player, from, to));
			return;
		} else {
			player.sendMessage("Nothing interesting happens");
		}
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final Item[] item : FletchingDefinitions.MATERIALS) {
			for (final Item i : item) {
				list.add(i.getId());
			}
		}
		list.add(FletchingDefinitions.KNIFE.getId());
		return list.toArray(new int[list.size()]);
	}
}
