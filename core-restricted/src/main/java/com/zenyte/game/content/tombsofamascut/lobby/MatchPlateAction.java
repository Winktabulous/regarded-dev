package com.zenyte.game.content.tombsofamascut.lobby;

import com.zenyte.game.content.tombsofamascut.encounter.ScabarasEncounter;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class MatchPlateAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof final ScabarasEncounter scabarasEncounter) {
			scabarasEncounter.handleMatchPlate(player, object);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {45360};
	}

	@Override
	public int getStrategyDistance(WorldObject obj) {
		return 1;
	}
}
