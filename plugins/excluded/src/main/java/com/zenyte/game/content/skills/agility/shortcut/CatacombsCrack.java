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

public class CatacombsCrack implements Shortcut {
	
	private static final Map<Integer, Location> SPOTS = new HashMap<Integer, Location>();
	
	private static final Animation ENTER = new Animation(746);
	private static final Animation EXIT = new Animation(748);
	
	private static final Location NECH_NORTH_EXIT = new Location(1706, 10078, 0);
	private static final Location NECH_SOUTH_EXIT = new Location(1716, 10056, 0);
	private static final Location ANKOU_NORTH_EXIT = new Location(1648, 10009, 0);
	private static final Location ANKOU_SOUTH_EXIT = new Location(1646, 10000, 0);
	
	private static final int NECH_CRACK_NORTH = 27961181;
	private static final int NECH_CRACK_SOUTH = 28125001;
	private static final int ANKOU_CRACK_NORTH = 27010840;
	private static final int ANKOU_CRACK_SOUTH = 26978065;
	
	static {
		SPOTS.put(NECH_CRACK_NORTH, NECH_SOUTH_EXIT);
		SPOTS.put(NECH_CRACK_SOUTH, NECH_NORTH_EXIT);
		SPOTS.put(ANKOU_CRACK_NORTH, ANKOU_SOUTH_EXIT);
		SPOTS.put(ANKOU_CRACK_SOUTH, ANKOU_NORTH_EXIT);
	}


	@Override
	public void startSuccess(Player player, WorldObject object) {
		final Location spot = SPOTS.get(object.getPositionHash());
		
		if(spot == null)
			return;
		
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0)
					player.setAnimation(ENTER);
				if(ticks == 1) {
					player.setAnimation(EXIT);
					player.setLocation(spot);
				} else if(ticks == 2)
					stop();
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(WorldObject object) {
		switch(object.getPositionHash()) {
			case NECH_CRACK_NORTH:
			case NECH_CRACK_SOUTH:
				return 34;
				
			case ANKOU_CRACK_NORTH:
			case ANKOU_CRACK_SOUTH:
				return 17;
			
			default:
				return 1;
		}
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 28892 };
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 3;
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 0;
	}

}
