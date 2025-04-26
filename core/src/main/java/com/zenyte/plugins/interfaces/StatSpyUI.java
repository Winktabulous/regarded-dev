package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 17. veebr 2018 : 2:12.17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class StatSpyUI implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		if (componentId == 95) {
			player.getInterfaceHandler().sendInterface(InterfacePosition.SPELLBOOK_TAB, 218);
		}
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 523 };
	}

}
