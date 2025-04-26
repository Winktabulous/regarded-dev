package com.zenyte.plugins.itemonitem;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

import java.util.Map;

/**
 * @author Kris | 27. march 2018 : 22:55.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GnomishFirelighterCoatingAction implements ItemOnItemAction {

	private static final Map<Integer, Integer> LIGHTER2LOGS = ImmutableMap.<Integer, Integer>
		builder().put(7329, 7404).put(7330, 7405).put(7331, 7406).put(10326, 10329).put(10327, 10328).build();
	
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (from.getId() != 1511 && to.getId() != 1511) {
			return;
		}
		final Item logs = from.getId() == 1511 ? from : to;
		final Item firelighter = logs == from ? to : from;
		final Integer colouredLogs = LIGHTER2LOGS.get(firelighter.getId());
		if (colouredLogs == null) {
			return;
		}
		logs.setId(colouredLogs);
		player.getInventory().deleteItem(firelighter.getId(), 1);
		player.getInventory().refresh(logs == from ? fromSlot : toSlot);
		player.sendMessage("You coat the logs with the " + firelighter.getDefinitions().getName().replace(" firelighter", "").toLowerCase() + " chemicals.");
	}

	@Override
	public int[] getItems() {
		return new int[] { 1511, 7329, 7330, 7331, 10326, 10327 };
	}

}
