package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class CorsairCoveRocks implements Shortcut {
	
	private static final Animation CLIMB = new Animation(839);
	private static final Location NORTH = new Location(2546, 2873, 0);
	private static final Location SOUTH = new Location(2546, 2871, 0);
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.faceObject(object);
		final boolean north = player.getY() > object.getY(); // north, going south
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(CLIMB);
					player.setForceMovement(new ForceMovement(north ? SOUTH : NORTH, 60, north ? ForceMovement.SOUTH : ForceMovement.NORTH));
				} else if(ticks == 2) {
					player.setLocation(north ? SOUTH : NORTH);
					stop();
				}
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public String getEndMessage(final boolean success) {
		return success ? "You climb over the rocks." : null;
	}
	
	
	// no level found online
	@Override
	public int getLevel(final WorldObject object) {
		return 30;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 31757 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 2;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 1;
	}
}
