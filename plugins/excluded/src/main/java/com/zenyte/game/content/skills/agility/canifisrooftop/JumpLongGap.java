package com.zenyte.game.content.skills.agility.canifisrooftop;

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

public final class JumpLongGap extends AgilityCourseObstacle {
	
	
	private static final Animation JUMP = new Animation(2583);
	private static final Animation CLIMB = new Animation(2585);
	
	private static final Location START = new Location(3487, 3499, 2);
	private static final Location FINISH = new Location(3479, 3499, 3);
	private static final Location LEDGE = new Location(3482, 3499, 3);
	private static final ForceMovement MOVE = new ForceMovement(FINISH, 45, ForceMovement.WEST);

	public JumpLongGap() {
		super(CanifisRooftopCourse.class, 3);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.setFaceLocation(FINISH);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(JUMP);
				} else if(ticks == 1) {
					player.setLocation(LEDGE);
					player.setAnimation(CLIMB);
				} else if(ticks == 2) {
					player.setForceMovement(MOVE);
				} else if(ticks == 3) {
					player.setLocation(FINISH);
					MarkOfGrace.spawn(player, CanifisRooftopCourse.MARK_LOCATIONS, 40, 40);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 40;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.GAP_14848 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 3;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 10;
	}	
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START;
	}
	
}
