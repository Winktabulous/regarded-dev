package com.zenyte.game.world.entity.player.dialogue.impl;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.Optional;

public class NPCChat extends Dialogue {

	private final String message;
	private final Runnable runnable;
	private final boolean showContinue;

	public NPCChat(final Player player, final int npcId, final String message, final boolean showContinue, final Runnable runnable) {
		super(player, npcId);
		this.message = message;
		this.showContinue = showContinue;
		this.runnable = runnable;
	}

	public NPCChat(final Player player, final int npcId, final String message, final Runnable runnable) {
		super(player, npcId);
		this.message = message;
		this.showContinue = true;
		this.runnable = runnable;
	}

	public NPCChat(final Player player, final int npcId, final String message) {
		this(player, npcId, message, null);
	}

	public NPCChat(final Player player, final int npcId, final NPC npc, final String message) {
		this(player, npcId, message, null);
		this.npc = npc;
	}

	public NPCChat(final Player player, final int npcId, final Optional<NPC> npc, final String message) {
		this(player, npcId, message, null);
		this.npc = npc.orElse(null);
	}

	public NPCChat(final Player player, final int npcId, final String message, final boolean showContinue) {
		this(player, npcId, message, showContinue, null);
	}

	@Override
	public void buildDialogue() {
		if (runnable != null) {
			npc(message, showContinue).executeAction(runnable);
		} else {
			npc(message, showContinue);
		}
	}
}