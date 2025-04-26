package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class TaverleyDungeonRailing implements Shortcut {
	
	private static final Animation SQUEEZE = new Animation(1237);
	private static final Location EAST = new Location(2936, 9810, 0);
	
	@Override
	public void startSuccess(Player player, WorldObject object) {
		player.faceObject(object);
		final boolean west = player.getLocation().getPositionHash() == object.getPositionHash();
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(SQUEEZE);
					player.setForceMovement(new ForceMovement(west ? EAST : object, 60, west ? ForceMovement.EAST : ForceMovement.WEST));
				} else if(ticks == 2) {
					player.setLocation(west ? EAST : object);
					stop();
				}
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	// no level found
	public int getLevel(WorldObject object) {
		return 30;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 28849 };
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
