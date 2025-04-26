package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class RockStepsAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getAppearance().getRenderAnimation() != null && player.getAppearance().getRenderAnimation().getWalk() == 772) {
			player.setLocation(object.getLocation().transform(0, object.getRotation() == 0 ? 1 : -1));
			player.getAppearance().resetRenderAnimation();
			player.setRunSilent(false);
			player.sendMessage("You use the steps to get yourself back onto the island.");
		} else {
			player.sendMessage("The eyes looking at you from below the surface make you reconsider going down there.");
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {45509};
	}
}
