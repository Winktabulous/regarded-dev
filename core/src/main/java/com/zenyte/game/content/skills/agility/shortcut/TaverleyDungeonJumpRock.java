package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class TaverleyDungeonJumpRock implements Shortcut {
	
	private static final Animation JUMP_UP = new Animation(2583);
	private static final Animation JUMP_DOWN = new Animation(2586);
	private static final Animation LAND = new Animation(2588);
	
	private static final Location TOP = new Location(2888, 9823, 1);
	private static final Location ROCK = new Location(2887, 9823, 0);
	private static final Location BOTTOM = new Location(2886, 9823, 0);
	
	@Override
	public void startSuccess(Player player, WorldObject object) {
		player.faceObject(object);
		final boolean direction = player.getPlane() == 0;
		WorldTasksManager.schedule(new WorldTask() {
			
			private int ticks;
			
			@Override
			public void run() {
				if(direction) {
					if(ticks == 0) {
						player.setAnimation(Animation.JUMP);
						player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction ? ForceMovement.EAST : ForceMovement.WEST));
					} if(ticks == 1) {
						player.setLocation(object);
						player.setAnimation(JUMP_UP);
					} if(ticks == 2) {
						player.setAnimation(LAND);
						player.setLocation(TOP);
					} if(ticks == 3)
						stop();
					
				} else {
					if(ticks == 0) 
						player.setAnimation(JUMP_DOWN);
					else if(ticks == 1) {
						player.setLocation(ROCK);
						player.setAnimation(LAND);
					} else if(ticks == 2) {
						player.setAnimation(Animation.JUMP);
						player.setForceMovement(new ForceMovement(player.getLocation(), 15, BOTTOM, 35, direction ? ForceMovement.EAST : ForceMovement.WEST));
					} else if(ticks == 3) {
						player.setLocation(BOTTOM);
						stop();
					}
				}
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	// no level found
	public int getLevel(WorldObject object) {
		return 70;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 154, 14106 };
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 4;
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 0;
	}

}
