package com.zenyte.plugins.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class MenuD extends Dialogue {

	private final String title;
	private final String[] options;

	public MenuD(final Player player, final String title, final String... options) {
		super(player);
		this.title = title;
		this.options = options;
	}

	public boolean isClickable() {
		return false;
	}

	public boolean cancelOption() {
		return false;
	}

	@Override
	public final void buildDialogue() {
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 187);
		player.setCloseInterfacesEvent(() -> player.getPacketDispatcher().sendClientScript(2158));
		if (isClickable()) {
			player.getPacketDispatcher().sendComponentSettings(187, 3, 0, 127, AccessMask.CONTINUE);
		}
		final StringBuilder builder = new StringBuilder();
		for (final String string : options) {
			builder.append(string).append("|");
		}
		if (cancelOption()) {
			builder.append("Cancel|");
		}
		builder.delete(builder.lastIndexOf("|"), builder.lastIndexOf("|") + 1);
		player.getPacketDispatcher().sendClientScript(217, title, builder.toString(), isClickable() ? 1 : 0);
	}

	public String[] getOptions() {
		return options;
	}
}
