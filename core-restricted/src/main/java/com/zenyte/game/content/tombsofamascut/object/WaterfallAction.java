package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class WaterfallAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof CrondisPuzzleEncounter crondisPuzzleEncounter) {
			crondisPuzzleEncounter.handleWaterfall(player, object);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {CrondisPuzzleEncounter.WATERFALL_OBJECT_ID, CrondisPuzzleEncounter.WATERFALL_OBJECT_ID + 1};
	}
}
