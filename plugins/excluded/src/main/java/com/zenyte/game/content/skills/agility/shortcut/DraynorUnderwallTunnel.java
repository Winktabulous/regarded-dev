package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class DraynorUnderwallTunnel implements Shortcut {

	/* Animations */
	private static final Animation DUCK = new Animation(2589);
	private static final Animation INVISIBLE = new Animation(2590);
	private static final Animation EMERGE = new Animation(2591);
	
	private static final Location WEST_HOLE = new Location(3065, 3260, 0);
	private static final Location EAST_HOLE = new Location(3070, 3260, 0);
	
	private static final Location WEST_START = new Location(3065, 3260, 0);
	private static final Location EAST_START = new Location(3070, 3260, 0);
	
	private static final ForceMovement GO_EAST = new ForceMovement(EAST_HOLE, 180, ForceMovement.EAST);
	private static final ForceMovement GO_WEST = new ForceMovement(WEST_HOLE, 180, ForceMovement.WEST);
	
	@Override
	public void startSuccess(Player player, WorldObject object) {
		final boolean direction = object.getId() == 19032;
		player.setAnimation(DUCK);
		player.setForceMovement(direction ? GO_EAST : GO_WEST);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 1)
					player.setAnimation(INVISIBLE);
				else if(ticks == 4)
					player.setAnimation(EMERGE);
				else if(ticks == 5) {
					player.setLocation(direction ? EAST_HOLE : WEST_HOLE);
					stop();

				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(WorldObject object) {
		return 42;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 19032, 19036 };
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
		return object.getId() == 19032 ? WEST_START : EAST_START;
	}

	
}
