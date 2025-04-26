package com.zenyte.plugins.interfaces.dialogue;

import com.zenyte.game.model.polls.PollManager;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 10. juuli 2018 : 21:32:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PollInterface implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		player.getPollManager().handleInterface(interfaceId, componentId, slotId);
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { PollManager.INTERFACE_ID, PollManager.VOTING_INTERFACE_ID, PollManager.HISTORY_INTERFACE_ID };
	}

}
