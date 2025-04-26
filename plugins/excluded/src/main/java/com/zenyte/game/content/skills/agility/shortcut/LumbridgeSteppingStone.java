package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class LumbridgeSteppingStone implements Shortcut {
	
	private static final Location NORTH = new Location(3212, 3137, 0);
	private static final Location SOUTH = new Location(3214, 3132, 0);
	private static final Location NORTH_FACE = new Location(3212, 3136, 0);

	@Override
	public boolean preconditions(final Player player, final WorldObject object) {
		if (!DiaryUtil.eligibleFor(DiaryReward.EXPLORERS_RING3, player)) {
			player.sendMessage("You need to have completed the hard Lumbridge and Draynor diaries to use this shortcut.");
			return false;
		}
		return true;
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final boolean direction = player.getY() >= 3135;
		player.setFaceLocation(direction ? NORTH_FACE : object);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction ? ForceMovement.SOUTH : ForceMovement.NORTH));
				} else if(ticks == 1) {
					player.setLocation(object);
				} else if(ticks == 2) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, direction ? SOUTH : NORTH, 35, direction ? ForceMovement.SOUTH : ForceMovement.NORTH));
				} else if (ticks == 3) {
					player.setLocation(direction ? SOUTH : NORTH);
					stop();
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 66;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 16513 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}
	
	@Override
	public double getSuccessXp(final WorldObject object) {
		return 0;
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return player.getY() >= 3135 ? NORTH : SOUTH;
	}

}
