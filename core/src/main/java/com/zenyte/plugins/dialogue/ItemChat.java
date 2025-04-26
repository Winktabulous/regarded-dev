package com.zenyte.plugins.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class ItemChat extends Dialogue {
	
	private final String message;
	private final int itemId;

	public ItemChat(Player player, Item item, String message) {
		this(player, item.getId(), message);
	}

	public ItemChat(Player player, int itemId, String message) {
		super(player);
		this.message = message;
		this.itemId = itemId;
	}

	@Override
	public void buildDialogue() {
		item(itemId, message);
	}
}