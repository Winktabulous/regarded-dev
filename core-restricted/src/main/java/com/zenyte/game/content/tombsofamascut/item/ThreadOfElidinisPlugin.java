package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

@SuppressWarnings("unused")
public class ThreadOfElidinisPlugin extends ItemPlugin {

	@Override
	public void handle() {
		bind("Inspect", (player, item, slotId) -> player.getDialogueManager().item(ThreadOfElidinisOnRunePouch.THREAD_OF_ELIDINIS, "This thread looks like it could be used to augment a rune pouch."));
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.THREAD_OF_ELIDINIS};
	}

}
