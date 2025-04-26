package com.zenyte.plugins.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 29. nov 2017 : 22:55.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public abstract class OptionsMenuD extends MenuD {

	public OptionsMenuD(final Player player, final String title, final String... options) {
		super(player, title, options);
	}

	public abstract void handleClick(final int slotId);

	public final void handleInterface(final int slotId) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		player.getPacketDispatcher().sendClientScript(2158);
		if (slotId != getOptions().length) {
			handleClick(slotId);
		}
	}

	@Override
	public boolean isClickable() {
		return true;
	}

}
