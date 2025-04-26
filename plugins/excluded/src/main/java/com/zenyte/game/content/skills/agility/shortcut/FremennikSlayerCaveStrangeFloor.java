package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class FremennikSlayerCaveStrangeFloor implements Shortcut {
	
	private static final Animation RUN = new Animation(1995);
	private static final Animation JUMP = new Animation(1603);
	
	private static final Location EAST_START_WEST = new Location(2771, 10002, 0);
	private static final Location EAST_START_EAST = new Location(2777, 10003, 0);
	private static final Location EAST_FINISH_WEST = new Location(2775, 10003, 0);
	private static final Location EAST_FINISH_EAST = new Location(2773, 10003, 0);
	
	private static final Location WEST_CRACK = new Location(2769, 10002, 0);
	private static final Location WEST_START_WEST = new Location(2766, 10002, 0);
	private static final Location WEST_START_EAST = new Location(2772, 10002, 0);
	private static final Location WEST_FINISH_WEST = new Location(2770, 10002, 0);
	private static final Location WEST_FINISH_EAST = new Location(2768, 10002, 0);

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.faceObject(object);
		final boolean crack = object.getPositionHash() == WEST_CRACK.getPositionHash(); // true if west crack
		final boolean west = player.getX() < object.getX(); // if player is west of object, going east
		final Location finish = crack ? (west ? WEST_FINISH_WEST : WEST_FINISH_EAST) : (west ? EAST_FINISH_WEST : EAST_FINISH_EAST);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(RUN);
					player.setForceMovement(new ForceMovement(object, 60, west ? ForceMovement.EAST : ForceMovement.WEST));
				} else if(ticks == 1)
					player.setAnimation(JUMP);
				else if(ticks == 2)
					player.setForceMovement(new ForceMovement(finish, 15, west ? ForceMovement.EAST : ForceMovement.WEST));
				else if(ticks == 3)
					player.setLocation(finish);
				else if(ticks == 4)
					stop();
				ticks++;
			}
			
		}, 0, 0);
	}
	
	// no level found
	@Override
	public int getLevel(final WorldObject object) {
		return 81;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 16544 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 5;
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		final boolean west = object.getPositionHash() == WEST_CRACK.getPositionHash();
		return player.getX() < object.getX() ? (west ? WEST_START_WEST : EAST_START_WEST ) : (west ? WEST_START_EAST : EAST_START_EAST);
	}

}
