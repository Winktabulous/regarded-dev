package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.ObjectDefinitions;

public class BrimhavenSteppingStone implements Shortcut {
	
	private static final Animation JUMP = new Animation(741);
	
	private static final Location NORTHWEST = new Location(2682, 9548, 0);
	private static final Location NORTHEAST = new Location(2690, 9547, 0);
	private static final Location SOUTH_NORTH = new Location(2695, 9533, 0);
	private static final Location SOUTH_SOUTH = new Location(2697, 9525, 0);
	
	private static final Location NORTH_ROCK_WEST = new Location(2688, 9547, 0);
	private static final Location NORTH_ROCK_MIDDLE = new Location(2686, 9548, 0);
	private static final Location NORTH_ROCK_EAST = new Location(2684, 9548, 0);
	
	private static final Location SOUTH_ROCK_NORTH = new Location(2695, 9531, 0);
	private static final Location SOUTH_ROCK_MIDDLE = new Location(2695, 9529, 0);
	private static final Location SOUTH_ROCK_SOUTH = new Location(2696, 9527, 0);

	@Override
	public boolean preconditions(final Player player, final WorldObject object) {
		if (!DiaryUtil.eligibleFor(DiaryReward.KARAMJA_GLOVES4, player)) {
			player.sendMessage("You need to have completed the elite Karamja diaries to use this shortcut.");
			return false;
		}
		return true;
	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final float preciseX = object.getPreciseCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation());
		final float preciseY = object.getPreciseCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation());
		final int direction = DirectionUtil.getFaceDirection(preciseX - player.getX(), preciseY - player.getY());
		final boolean north = object.getY() > SOUTH_ROCK_NORTH.getY();
		final boolean side = north ? object.getX() == 2688 : object.getX() == 2695;
		final Location finish = north ? (side ? NORTH_ROCK_EAST : NORTH_ROCK_WEST) : (side ? SOUTH_ROCK_SOUTH : SOUTH_ROCK_NORTH);
		final Location shore = north ? (side ? NORTHWEST : NORTHEAST) : (side ? SOUTH_SOUTH : SOUTH_NORTH);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction));
				} else if(ticks == 1) {
					player.setLocation(object);
				} else if(ticks == 2) {
					player.setAnimation(JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, north ? NORTH_ROCK_MIDDLE : SOUTH_ROCK_MIDDLE, 35, direction));
				} else if(ticks == 3)
					player.setLocation(north ? NORTH_ROCK_MIDDLE : SOUTH_ROCK_MIDDLE);
				else if(ticks == 4) {
					player.setAnimation(JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, finish, 35, direction));
				} else if(ticks == 5)
					player.setLocation(finish);
				else if(ticks == 6) {
					player.setAnimation(JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, shore, 35, direction));
				} else if(ticks == 7) {
					player.getAchievementDiaries().update(KaramjaDiary.CROSS_THE_LAVA);
					player.setLocation(shore);
					stop();
				}
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		final boolean north = object.getY() > SOUTH_ROCK_NORTH.getY();
		final boolean side = north ? object.getX() == 2688 : object.getX() == 2695;
		return north ? (side ? NORTHEAST : NORTHWEST) : (side ? SOUTH_NORTH : SOUTH_SOUTH);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 83;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 19040 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 8;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 0;
	}
}
