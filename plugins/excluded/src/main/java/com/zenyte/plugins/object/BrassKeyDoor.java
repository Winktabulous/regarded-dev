package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.Door;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 29. mai 2018 : 16:43:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BrassKeyDoor implements ObjectAction {

	private static final Location OUTSIDE = new Location(3115, 3449, 0);

	
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		if (player.getLocation().getPositionHash() == OUTSIDE.getPositionHash()
                && !player.getInventory().containsItem(983, 1)) {
			player.sendMessage("You need a key to unlock the door.");
			return;
		}
		player.lock(3);
		player.addWalkSteps(object.getX(), object.getY());
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			private WorldObject door;
			@Override
			public void run() {
				switch(ticks++) {
				case 0:
					door = Door.handleGraphicalDoor(object, null);
					return;
				case 1:
					if (player.getLocation().getPositionHash() != OUTSIDE.getPositionHash()) {
						player.addWalkSteps(door.getX(), door.getY(), 1, false);
					} else {
						player.addWalkSteps(object.getX(), object.getY(), 1, false);
					}
					return;
				case 3:
					Door.handleGraphicalDoor(door, object);
					stop();
					return;
				}
			}
			
		}, 0, 0);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { 1804 };
	}

}
