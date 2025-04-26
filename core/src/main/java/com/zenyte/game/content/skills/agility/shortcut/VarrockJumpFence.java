package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class VarrockJumpFence implements Shortcut {
	private static final Animation RUN = new Animation(1995);
	private static final Animation JUMP = new Animation(1603);
	private static final Location NORTH = new Location(3240, 3335, 0);
	private static final Location SOUTH = new Location(3240, 3334, 0);
	private static final Location START_NORTH = new Location(3240, 3338, 0);
	private static final Location START_SOUTH = new Location(3240, 3331, 0);
	private static final ForceMovement RUN_NORTH = new ForceMovement(NORTH, 60, ForceMovement.SOUTH);
	private static final ForceMovement RUN_SOUTH = new ForceMovement(SOUTH, 60, ForceMovement.NORTH);
	private static final ForceMovement JUMP_NORTH = new ForceMovement(SOUTH, 15, ForceMovement.SOUTH);
	private static final ForceMovement JUMP_SOUTH = new ForceMovement(NORTH, 15, ForceMovement.NORTH);

	@Override
	public int getLevel(WorldObject object) {
		return 10;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {16518};
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 4;
	}

	@Override
	public void startSuccess(Player player, WorldObject object) {
		final boolean north = player.getY() >= 3335;
		player.lock();
		player.setFaceLocation(north ? NORTH : SOUTH);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				if (ticks == 0) {
					player.setAnimation(RUN);
					player.setForceMovement(north ? RUN_NORTH : RUN_SOUTH);
				} else if (ticks == 1) {
					player.setAnimation(JUMP);
				} else if (ticks == 2) {
					player.setForceMovement(north ? JUMP_NORTH : JUMP_SOUTH);
				} else if (ticks == 3) {
					player.setLocation(north ? SOUTH : NORTH);
				} else if (ticks == 4) {
					player.getAchievementDiaries().update(VarrockDiary.JUMP_OVER_VARROCK_FENCE);
					player.unlock();
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 0;
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return player.getY() >= 3335 ? START_NORTH : START_SOUTH;
	}
}
