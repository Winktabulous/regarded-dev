package com.zenyte.game.content.tombsofamascut.lobby;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class InvocationBoardAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (!"read".equalsIgnoreCase(option)) {
			return;
		}
		GameInterface.TOA_INVOCATION_INFO.open(player);
	}

	@Override public Object[] getObjects() {
		return new Object[] {46073};
	}
}
