package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class FaladorCrumblingWall implements Shortcut {

	private static final Animation CLIMB = new Animation(840);
	
	private static final Location WEST = new Location(2934, 3355, 0);
	private static final Location EAST = new Location(2936, 3355, 0);
	
	private static final ForceMovement GO_WEST = new ForceMovement(WEST, 60, ForceMovement.WEST);
	private static final ForceMovement GO_EAST = new ForceMovement(EAST, 60, ForceMovement.EAST);

	@Override
	public void startSuccess(Player player, WorldObject object) {
		final boolean direction = player.getX() < 2935;		
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(CLIMB);
					player.setForceMovement(direction ? GO_EAST : GO_WEST);
				} else if(ticks == 2) {
					player.setLocation(direction ? EAST : WEST);
					player.getAchievementDiaries().update(FaladorDiary.CLIMB_OVER_FALADOR_WALL);
					stop();
				}
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(WorldObject object) {
		return 5;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 24222 };
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
