package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.tombsofamascut.encounter.ScabarasEncounter;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class AncientButtonAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof final ScabarasEncounter encounter) {
			encounter.pressAncientButton(player);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {ScabarasEncounter.PRESSURE_BUTTON_ID};
	}
}
