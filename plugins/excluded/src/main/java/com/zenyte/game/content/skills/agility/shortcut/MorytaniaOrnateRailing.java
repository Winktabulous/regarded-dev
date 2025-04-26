package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class MorytaniaOrnateRailing implements Shortcut {

	private static final Animation SQUEEZE = new Animation(746);
	
	private static final Location BRIDGE_NORTH = new Location(3425, 3484, 0);
	private static final Location BRIDGE_SOUTH = new Location(3425, 3483, 0);
	private static final Location HILL_WEST = new Location(3423, 3476, 0);
	private static final Location HILL_EAST = new Location(3424, 3476, 0);

	private static final ForceMovement GO_NORTH = new ForceMovement(BRIDGE_NORTH, 50, ForceMovement.NORTH);
	private static final ForceMovement GO_SOUTH = new ForceMovement(BRIDGE_SOUTH, 50, ForceMovement.SOUTH);
	private static final ForceMovement GO_WEST = new ForceMovement(HILL_WEST, 50, ForceMovement.WEST);
	private static final ForceMovement GO_EAST = new ForceMovement(HILL_EAST, 50, ForceMovement.EAST);
	
	@Override
	public void startSuccess(Player player, WorldObject object) {
		player.faceObject(object);
		final boolean bridge = object.getId() == 17000;
		final boolean direction = player.getLocation().getPositionHash() == object.getPositionHash();		
		WorldTasksManager.schedule(new WorldTask() {
			
			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(SQUEEZE);
					player.setForceMovement(bridge ? (direction ? GO_SOUTH : GO_NORTH) : (direction ? GO_WEST : GO_EAST));
				} else if(ticks == 1) {
					player.setLocation(bridge ? (direction ? BRIDGE_SOUTH : BRIDGE_NORTH) : (direction ? HILL_WEST : HILL_EAST));
					stop();
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(WorldObject object) {
		return 65;
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 2;
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 0;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] { 16552, 17000 };
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		final boolean hill = object.getId() == 16552;
		return hill ? (player.getX() < 3424 ? HILL_WEST : HILL_EAST) : (player.getY() >= 3484 ? BRIDGE_NORTH : BRIDGE_SOUTH);
	}
	
}
