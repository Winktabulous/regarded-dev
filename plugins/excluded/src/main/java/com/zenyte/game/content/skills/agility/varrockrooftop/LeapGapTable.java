package com.zenyte.game.content.skills.agility.varrockrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public final class LeapGapTable extends AgilityCourseObstacle {

	private static final Animation RUN = new Animation(1995);
	private static final Animation CLIMB = new Animation(2585);
	private static final Animation JUMP = new Animation(4789);
	private static final Location TABLE = new Location(3215, 3399, 3);
	private static final Location LEDGE = new Location(3217, 3399, 3);
	private static final Location FINISH = new Location(3218, 3399, 3);
	private static final ForceMovement JUMP_FM = new ForceMovement(TABLE, 45, 1446);
	private static final ForceMovement TABLE_LEAP_FM = new ForceMovement(TABLE, 15, LEDGE, 35, ForceMovement.EAST);
	private static final ForceMovement CLIMB_FM = new ForceMovement(FINISH, 30, ForceMovement.EAST);

	public LeapGapTable() {
		super(VarrockRooftopCourse.class, 6);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.setFaceLocation(TABLE);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				if (ticks == 0) {
					player.setAnimation(RUN);
				} else if(ticks == 1) {
					player.setAnimation(JUMP);
					player.setForceMovement(JUMP_FM);
				} else if(ticks == 2) {
					player.setLocation(TABLE);
					player.setForceMovement(TABLE_LEAP_FM);
				} else if(ticks == 3) {
					player.setLocation(LEDGE);
					player.setAnimation(CLIMB);
				} else if(ticks == 4)
					player.setForceMovement(CLIMB_FM);
				else if(ticks == 5) {
					player.setLocation(FINISH);
					MarkOfGrace.spawn(player, VarrockRooftopCourse.MARK_LOCATIONS, 40, 30);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 30;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 6;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 22;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.GAP_14834 };
	}
	
}
