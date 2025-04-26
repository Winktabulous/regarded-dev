package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.tombsofamascut.encounter.ApmekenEncounter;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class RoofSupportAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof ApmekenEncounter encounter) {
			encounter.repairRoof(player, object);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {45494};
	}
}
