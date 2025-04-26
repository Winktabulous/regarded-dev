package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16. dets 2017 : 18:04.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WarriorGuildCatapultInterface implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		player.getVarManager().sendBit(2247, componentId == 12 ? 8 : componentId - 8);
		player.getTemporaryAttributes().put("catapultStance", componentId == 12 ? 8 : componentId - 8);
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 411 };
	}

}
