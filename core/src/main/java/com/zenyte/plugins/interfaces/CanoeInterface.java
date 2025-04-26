package com.zenyte.plugins.interfaces;

import com.zenyte.game.content.skills.woodcutting.actions.CanoeHandler;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 7 feb. 2018 : 18:10:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class CanoeInterface implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		if (interfaceId == 416) {
			CanoeHandler.shapeCanoeStation(player, componentId);
		} else {
			CanoeHandler.paddleCanoe(player, componentId);
		}
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 416, 57 };
	}

}
