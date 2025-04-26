package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.util.HashMap;
import java.util.Map;

public class JumpFromWall implements Shortcut {
	
	private static final Map<Integer, Location> WALLS = new HashMap<Integer, Location>();
	
	private static final Animation JUMP = new Animation(2586);
	private static final Animation LAND = new Animation(2588);
	
	private static final Location FALLY_NORTH_WALL = new Location(3033, 3390, 1);
	private static final Location FALLY_SOUTH_WALL = new Location(3032, 3388, 1);
	private static final Location YANILLE_NORTH_WALL = new Location(2556, 3075, 1);
	private static final Location YANILLE_SOUTH_WALL = new Location(2556, 3072, 1);
	
	private static final Location FALLY_NORTH_FINISH = new Location(3033, 3390, 0);
	private static final Location FALLY_SOUTH_FINISH = new Location(3032, 3388, 0);
	private static final Location YANILLE_NORTH_FINISH = new Location(2556, 3075, 0);
	private static final Location YANILLE_SOUTH_FINISH = new Location(2556, 3072, 0);
	
	static {
		WALLS.put(FALLY_NORTH_WALL.getPositionHash(), FALLY_NORTH_FINISH);
		WALLS.put(FALLY_SOUTH_WALL.getPositionHash(), FALLY_SOUTH_FINISH);
		WALLS.put(YANILLE_NORTH_WALL.getPositionHash(), YANILLE_NORTH_FINISH);
		WALLS.put(YANILLE_SOUTH_WALL.getPositionHash(), YANILLE_SOUTH_FINISH);

	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		if(WALLS.get(object.getPositionHash()) == null)
			return;
		
		player.faceObject(object);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0)
					player.setAnimation(JUMP);
				else if(ticks == 1) {
					player.setAnimation(LAND);
					player.setLocation(WALLS.get(object.getPositionHash()));
					stop();
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 0;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 17051, 17052, 17048 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 2;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 0;
	}
	
}
