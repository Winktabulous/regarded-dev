package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class PlainChat extends Dialogue {
	
	private final String message;
	private final boolean showContinue;

	public PlainChat(final Player player, final String message) {
		this(player, message, true);
	}
	
	public PlainChat(final Player player, final String message, final boolean showContinue) {
		super(player);
		this.message = message;
		this.showContinue = showContinue;
	}

	@Override
	public void buildDialogue() {
		this.plain(message, showContinue);
	}
}