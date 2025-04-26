package com.zenyte.plugins.interfaces.dialogue;

import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 12. juuli 2018 : 14:33:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class NormalDialogueInterface implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		player.getDialogueManager().next();		
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 229, 11, 193, 233, 231, 217 };
	}

}
