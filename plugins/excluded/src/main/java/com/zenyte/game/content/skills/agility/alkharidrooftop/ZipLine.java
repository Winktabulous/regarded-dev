package com.zenyte.game.content.skills.agility.alkharidrooftop;

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

/**
 * @author Tommeh | 26 feb. 2018 : 17:42:47
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ZipLine extends AgilityCourseObstacle {
	
	private static final Location START_LOC = new Location(3301, 3163, 3);
	private static final Location FIRST_LOC = new Location(3303, 3163, 1);
	private static final Location END_LOC = new Location(3314, 3163, 1);
	private static final Location walkDestination = new Location(3315, 3163, 1);
	private static final Animation JUMP_ANIM = new Animation(2586, 10);
	private static final Animation TEETH_GRIP_ANIM = new Animation(1601);
	private static final Animation TEETH_GRIP_SWING_ANIM = new Animation(1602);
	private static final ForceMovement FORCE_MOVEMENT = new ForceMovement(END_LOC, 300, ForceMovement.EAST);

	public ZipLine() {
		super(AlKharidRooftopCourse.class, 4);
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.setAnimation(JUMP_ANIM);
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override
			public void run() {
				switch (ticks++) {
				case 0:
					player.setLocation(FIRST_LOC);
					player.setAnimation(TEETH_GRIP_ANIM);
					break;
				case 1:
					player.setAnimation(TEETH_GRIP_SWING_ANIM);
					player.setForceMovement(FORCE_MOVEMENT);
					break;
				case 6:
					player.setAnimation(Animation.STOP);
					player.setLocation(END_LOC);
					WorldTasksManager.schedule(() -> {
						player.addWalkSteps(walkDestination.getX(), walkDestination.getY(), 1, true);
						MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20);
					});
					stop();
					break;
				}
			}
		}, 0, 1);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 20;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.ZIP_LINE_14403 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 40;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 14;
	}

}
