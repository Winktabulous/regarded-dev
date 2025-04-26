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

public final class LeapGapClimb extends AgilityCourseObstacle {
	private static final Animation CLIMB = new Animation(2585);

	public LeapGapClimb() {
		super(VarrockRooftopCourse.class, 5);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final Location ledge = new Location(player.getX(), player.getY() - 3, 3);
		final Location end = new Location(player.getX(), player.getY() - 4, 3);
		player.setFaceLocation(ledge);
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				if (ticks == 1) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(ledge, 60, ForceMovement.SOUTH));
				} else if (ticks == 2) {
					player.setAnimation(CLIMB);
					player.setLocation(ledge);
				} else if (ticks == 3) {
					player.setForceMovement(new ForceMovement(end, 30, ForceMovement.SOUTH));
				} else if (ticks == 4) {
					player.setLocation(end);
					MarkOfGrace.spawn(player, VarrockRooftopCourse.MARK_LOCATIONS, 40, 30);
					player.unlock();
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
	public int[] getObjectIds() {
		return new int[] { ObjectId.GAP_14833 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 5;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 9;
	}
}
