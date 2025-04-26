package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class YanilleUnderwallTunnel implements Shortcut {

	/* Animations */
	private static final Animation DUCK = new Animation(2589);
	private static final Animation INVISIBLE = new Animation(2590);
	private static final Animation EMERGE = new Animation(2591);
	
	private static final Location NORTH = new Location(2575, 3112, 0);
	private static final Location SOUTH = new Location(2575, 3107, 0);
	
	private static final Location NORTH_START = new Location(2575, 3112, 0);
	private static final Location SOUTH_START = new Location(2575, 3107, 0);

	private static final ForceMovement GO_NORTH = new ForceMovement(NORTH, 180, ForceMovement.NORTH);
	private static final ForceMovement GO_SOUTH = new ForceMovement(SOUTH, 180, ForceMovement.SOUTH);
	
	@Override
	public void startSuccess(Player player, WorldObject object) {
		final boolean direction = object.getId() == 16519;
		player.setAnimation(DUCK);
		player.setForceMovement(direction ? GO_NORTH : GO_SOUTH);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 1)
					player.setAnimation(INVISIBLE);
				else if(ticks == 4)
					player.setAnimation(EMERGE);
				else if(ticks == 5) {
					player.setLocation(direction ? NORTH : SOUTH);
					stop();
				}
					
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(WorldObject object) {
		return 16;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 16519, 16520 };
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 6;
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 0;
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return object.getId() == 16519 ? SOUTH_START : NORTH_START;
	}
	
}
