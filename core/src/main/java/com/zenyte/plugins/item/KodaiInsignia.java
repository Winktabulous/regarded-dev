package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 25. aug 2018 : 22:32:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class KodaiInsignia extends ItemPlugin {

	@Override
	public void handle() {
		bind("Inspect", (player, item, slotId) -> player.getDialogueManager().start(new ItemChat(player, new Item(21043),
				"You sense a dark magic emenating from the insignia. It<br>looks like this could be attached to a wand.")));
	}

	@Override
	public int[] getItems() {
		return new int[] { 21043 };
	}

}
