package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 23. nov 2017 : 3:47.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HouseOptionsTab implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		if (player.isLocked()) {
			return;
		}
		switch(componentId) {
		case 1:
			player.getConstruction().getHouseViewer().openHouseViewer();
			return;
		case 5:
			player.getConstruction().setBuildingMode(true);
			break;
		case 6:
			player.getConstruction().setBuildingMode(false);
			break;
		case 8:
			player.getConstruction().setRenderDoorsOpen(true);
			break;
		case 9:
			player.getConstruction().setRenderDoorsOpen(false);
			break;
		case 11:
			player.getConstruction().setTeleportInside(true);
			break;
		case 12:
			player.getConstruction().setTeleportInside(false);
			break;
		}
		player.getConstruction().refreshHouseOptions();
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 370 };
	}

}
