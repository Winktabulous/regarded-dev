package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class PlayerChat extends Dialogue {
	
	private final String message;

	public PlayerChat(Player player, String message) {
		super(player);
		this.message = message;
	}

	@Override
	public void buildDialogue() {
		player(message);
	}
}