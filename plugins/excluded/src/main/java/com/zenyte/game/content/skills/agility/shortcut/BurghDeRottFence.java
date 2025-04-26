package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class BurghDeRottFence implements Shortcut {
	
	private static final Animation RUN = new Animation(1995);
	private static final Animation JUMP = new Animation(1603);
	
	private static final Location WEST = new Location(3473, 3221, 0);
	private static final Location EAST = new Location(3474, 3221, 0);
	
	private static final Location START_WEST = new Location(3471, 3221, 0);
	private static final Location START_EAST = new Location(3477, 3221, 0);
	
	private static final ForceMovement RUN_WEST = new ForceMovement(WEST, 60, ForceMovement.EAST);
	private static final ForceMovement RUN_EAST = new ForceMovement(EAST, 60, ForceMovement.WEST);
	
	private static final ForceMovement JUMP_WEST = new ForceMovement(EAST, 15, ForceMovement.EAST);
	private static final ForceMovement JUMP_EAST = new ForceMovement(WEST, 15, ForceMovement.WEST);

	@Override
	public int getLevel(final WorldObject object) {
		return 33;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 12776 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final boolean west = player.getX() < 3474;		
		player.setFaceLocation(west ? WEST : EAST);
		WorldTasksManager.schedule(new WorldTask() {
			
			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(RUN);
					player.setForceMovement(west ? RUN_WEST : RUN_EAST);
				} else if(ticks == 1)
					player.setAnimation(JUMP);
				else if(ticks == 2)
					player.setForceMovement(west ? JUMP_WEST : JUMP_EAST);
				else if (ticks == 3)
					player.setLocation(west ? EAST : WEST);
				else if (ticks == 4) {
					player.unlock();
					stop();
				}
				ticks++;
			}
			
		}, 0, 0);
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 0;
	}
	
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return player.getX() < object.getX() ? START_WEST : START_EAST;
	}
	
}
