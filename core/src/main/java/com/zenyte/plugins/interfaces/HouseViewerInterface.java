package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 23. nov 2017 : 3:50.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HouseViewerInterface implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		player.getConstruction().getHouseViewer().handleInterface(interfaceId, componentId, slotId - 1, itemId, optionId);
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 422 };
	}

}
