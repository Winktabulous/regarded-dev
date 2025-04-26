package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.PlayerChat;

/**
 * @author Tommeh | 26 aug. 2018 | 15:29:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ThreadOnNeedleItemAction implements ItemOnItemAction {

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		player.getDialogueManager().start(new PlayerChat(player, "Perhaps I should use the needle with a piece of leather instead."));
	}

	@Override
	public int[] getItems() {
		return new int[] { CraftingDefinitions.THREAD.getId(), CraftingDefinitions.NEEDLE.getId() };
	}

}
