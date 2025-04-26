package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.packet.out.IfSetAngle;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.npcs.NPCDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Arham 4 on 2/15/2016.
 * <p>
 * Represents a singular message with an NPC talking.
 */
public class NPCMessage implements Message {

	private static final Logger logger = LoggerFactory.getLogger(NPCMessage.class);

	private final Expression expression;
	private final int npcId;
	private final String message;
	private final boolean showContinue;
	private final String npcName;
	private Runnable onDisplay;
	private Runnable runnable;

	public NPCMessage(final int npcId, final Expression expression, final String message) {
		this(npcId, expression, message, true, null);
	}

	public NPCMessage(final int npcId, final Expression expression, final String message, final boolean showContinue) {
		this(npcId, expression, message, showContinue, null);
	}

	public NPCMessage(final int npcId, final Expression expression, final String message, final String npcName) {
		this(npcId, expression, message, true, npcName);
	}

	public NPCMessage(final int npcId, final Expression expression, final String message, final boolean showContinue, final String npcName) {
		this.npcId = npcId;
		this.expression = expression;
		this.message = message;
		this.showContinue = showContinue;
		this.npcName = npcName;
	}

	@Override
	public void executeAction(final Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void execute(final Player player) {
		if (runnable != null) {
			runnable.run();
		}
	}

	@Override
	public void display(final Player player) {
		final NPCDefinitions baseDefs = NPCDefinitions.get(npcId);
		if (baseDefs == null) {
			logger.warn("Couldn't find base def for NPC ID {}", npcId);
			return;
		}
		final NPCDefinitions transmogrifiedDefs = NPCDefinitions.get(player.getTransmogrifiedId(baseDefs, npcId));
		player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, 231);
		player.getPacketDispatcher().sendComponentNPCHead(231, 2, npcId);
		player.getPacketDispatcher().sendComponentText(231, 4, npcName == null ? transmogrifiedDefs.getName() : npcName);
		if (showContinue) {
			player.getPacketDispatcher().sendComponentText(231, 5, continueMessage(player));
		} else {
			player.getPacketDispatcher().sendComponentVisibility(231, 5, true);
		}
		player.getPacketDispatcher().sendComponentText(231, 6, message);
		player.getPacketDispatcher().sendClientScript(600, 1, 1, 16, 15138822);
		player.getPacketDispatcher().sendComponentAnimation(231, 2, expression.getId());
		final String toString = expression.toString();
		if (toString.startsWith("HIGH_REV")) {
			final String name = (npcName == null ? transmogrifiedDefs.getName() : npcName);
			final int zoom = name.contains("General ") || name.equalsIgnoreCase("Tiny Thom") ? 1000 : 2500;
			player.send(new IfSetAngle(231, 2, 0, 1900, zoom));
		} else if (toString.startsWith("EASTER_BUNNY") || toString.startsWith("EASTER_BIRD")) {
			player.send(new IfSetAngle(231, 2, 0, 1900, 2500));
		}
		if (onDisplay != null) {
			onDisplay.run();
		}
	}

	public void setOnDisplay(Runnable onDisplay) {
		this.onDisplay = onDisplay;
	}
}
