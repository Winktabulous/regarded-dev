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

public final class TallTree extends AgilityCourseObstacle {
	
	private static final Animation CLIMB = new Animation(1765);
	
	private static final Location START = new Location(3507, 3488, 0);
	private static final Location FORCE_START = new Location(3507, 3489, 0);
	private static final Location BRANCH = new Location(3508, 3489, 0);
	private static final Location FINISH = new Location(3506, 3492, 2);

	public TallTree() {
		super(CanifisRooftopCourse.class, 1);
	}

	@Override
	public void startSuccess(Player player, WorldObject object) {
		player.addWalkSteps(FORCE_START.getX(), FORCE_START.getY(), -1, false);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			private boolean start;
			
			@Override
			public void run() {
				if(player.getLocation().getPositionHash() == FORCE_START.getPositionHash()) {
					player.setFaceLocation(BRANCH);
					start = true;
				}
				if(start) {
					if(ticks == 0) {
						player.setAnimation(CLIMB);
					} else if(ticks == 2) {
						player.setForceMovement(new ForceMovement(object, 60, ForceMovement.EAST));
					} else if(ticks == 4) {
						player.setAnimation(Animation.STOP);
						player.setLocation(FINISH);
						MarkOfGrace.spawn(player, CanifisRooftopCourse.MARK_LOCATIONS, 40, 40);
						stop();
					}
					ticks++;
				}
			}
		}, 0, 0);
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 10;
	}

	@Override
	public int getLevel(WorldObject object) {
		return 40;
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 5;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.TALL_TREE_14843 };
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START;
	}

}
