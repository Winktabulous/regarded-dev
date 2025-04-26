package com.zenyte.game.content.boss.phantommuspah;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class PhantomEntrance implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (object.getId() == 46597) {
			PhantomInstance.start(player);
			return;
		}
		if (!player.inArea("Phantom instance")) {
			return;
		}
		final PhantomInstance instance = (PhantomInstance) player.getArea();
		instance.leave();
	}

	@Override public Object[] getObjects() {
		return new Object[] { 46597, 46598, };
	}
}
