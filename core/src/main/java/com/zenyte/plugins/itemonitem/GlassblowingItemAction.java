package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.skills.GlassBlowingD;

/**
 * @author Kris | 11. nov 2017 : 0:19.35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class GlassblowingItemAction implements ItemOnItemAction {

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		player.getDialogueManager().start(new GlassBlowingD(player));
	}

	@Override
	public int[] getItems() {
		return new int[] { 1775, 1785 };
	}

}