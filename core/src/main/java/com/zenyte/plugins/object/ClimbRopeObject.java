package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.Rope;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

public class ClimbRopeObject implements ObjectAction {
	private static final Animation CLIMB = new Animation(828);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		Rope rope = Rope.ROPES.get(object.getId());
		if (rope == null) return;
		player.faceObject(object);
		player.setAnimation(CLIMB);
		WorldTasksManager.schedule(() -> {
			player.setLocation(rope.getTile());
		}, 0);
	}

	@Override
	public Object[] getObjects() {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (final Rope entry : Rope.VALUES) {
			if (!list.contains(entry.getId())) {
				list.add(entry.getId());
			}
		}
		return list.toArray(new Object[list.size()]);
	}
}
