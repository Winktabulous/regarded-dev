package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 10 aug. 2018 | 18:54:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class RevenantCavePillar implements Shortcut {
	@Override
	public int getLevel(WorldObject object) {
		return object.getX() == 3220 ? 65 : object.getX() == 3241 ? 89 : 75;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {31561};
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 5;
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		final boolean vertical = object.getRotation() == 0;
		return vertical ? player.getY() > object.getY() ? new Location(object.getX(), object.getY() + 2, object.getPlane()) : new Location(object.getX(), object.getY() - 2, object.getPlane()) : player.getX() < object.getX() ? new Location(object.getX() - 2, object.getY(), object.getPlane()) : new Location(object.getX() + 2, object.getY(), object.getPlane());
	}

	@Override
	public void startSuccess(Player player, WorldObject object) {
		final boolean vertical = object.getRotation() == 0;
		final int direction = vertical ? player.getY() > object.getY() ? ForceMovement.SOUTH : ForceMovement.NORTH : player.getX() <= object.getX() ? ForceMovement.EAST : ForceMovement.WEST;
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override
			public void run() {
				if (ticks == 0) {
					final Location destination = direction == ForceMovement.NORTH ? new Location(player.getX(), player.getY() + 2, player.getPlane()) : direction == ForceMovement.SOUTH ? new Location(player.getX(), player.getY() - 2, player.getPlane()) : direction == ForceMovement.EAST ? new Location(player.getX() + 2, player.getY(), player.getPlane()) : new Location(player.getX() - 2, player.getY(), player.getPlane());
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, destination, 35, direction));
				} else if (ticks == 1) {
					player.setLocation(object);
				} else if (ticks == 2) {
					final Location destination = direction == ForceMovement.NORTH ? new Location(player.getX(), player.getY() + 2, player.getPlane()) : direction == ForceMovement.SOUTH ? new Location(player.getX(), player.getY() - 2, player.getPlane()) : direction == ForceMovement.EAST ? new Location(player.getX() + 2, player.getY(), player.getPlane()) : new Location(player.getX() - 2, player.getY(), player.getPlane());
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, destination, 35, direction));
				} else if (ticks == 3) {
					final Location destination = direction == ForceMovement.NORTH ? new Location(player.getX(), player.getY() + 2, player.getPlane()) : direction == ForceMovement.SOUTH ? new Location(player.getX(), player.getY() - 2, player.getPlane()) : direction == ForceMovement.EAST ? new Location(player.getX() + 2, player.getY(), player.getPlane()) : new Location(player.getX() - 2, player.getY(), player.getPlane());
					player.setLocation(destination);
					player.unlock();
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 0;
	}
}
