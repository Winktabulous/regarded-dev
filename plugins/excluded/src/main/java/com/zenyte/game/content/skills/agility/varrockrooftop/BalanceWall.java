package com.zenyte.game.content.skills.agility.varrockrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public final class BalanceWall extends AgilityCourseObstacle {

	private static final Animation RUN = new Animation(1995);
	private static final Animation HANG = new Animation(1122);
	private static final Animation HANG_QUICK = new Animation(1124);
	private static final Animation TURN = new Animation(753);	
	private static final RenderAnimation RENDER = new RenderAnimation(757, 757, 756, 756, 756, 756, -1);
	
	private static final Location START = new Location(3194, 3416, 1);
	private static final Location JUMP_SPOT = new Location(3193, 3416, 1);
	private static final Location WALL_END = new Location(3190, 3407, 1);
	private static final Location END = new Location(3192, 3406, 3);
	
	private static final Location[] WALLS = {
			new Location(3190, 3414, 1),
			new Location(3190, 3413, 1),
			new Location(3190, 3412, 1),
			new Location(3190, 3411, 1),
			new Location(3190, 3410, 1),
			
			new Location(3190, 3409, 1), // special tile, turn around
	};

	public BalanceWall() {
		super(VarrockRooftopCourse.class, 4);
	}

	@Override
	public void startSuccess(Player player, WorldObject object) {
		player.setFaceLocation(JUMP_SPOT);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(RUN);
					player.setForceMovement(new ForceMovement(JUMP_SPOT, 30, ForceMovement.WEST));
				} else if(ticks == 1)
					player.setLocation(JUMP_SPOT);
				else if(ticks == 2) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, WALLS[0], 35, ForceMovement.WEST));
				} else if(ticks == 3) {
					player.setAnimation(HANG);
					player.setLocation(WALLS[0]);
				} else if(ticks == 4)
					player.setForceMovement(new ForceMovement(player.getLocation(), 10, WALLS[1], 20, ForceMovement.WEST));
				else if(ticks == 5) {
					player.setAnimation(HANG);
					player.setLocation(WALLS[1]);
				} else if(ticks == 6)
					player.setForceMovement(new ForceMovement(player.getLocation(), 10, WALLS[2], 20, ForceMovement.WEST));
				else if(ticks == 7) {
					player.setAnimation(HANG);
					player.setLocation(WALLS[2]);
				} else if(ticks == 8)
					player.setForceMovement(new ForceMovement(player.getLocation(), 10, WALLS[3], 20, ForceMovement.WEST));
				else if(ticks == 9) {
					player.setAnimation(HANG);
					player.setLocation(WALLS[3]);
				} else if(ticks == 10)
					player.setForceMovement(new ForceMovement(player.getLocation(), 10, WALLS[4], 20, ForceMovement.WEST));
				else if(ticks == 11) {
					player.setAnimation(HANG);
					player.setLocation(WALLS[4]);
				} else if(ticks == 12) {
					player.setAnimation(HANG_QUICK);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, WALLS[5], 35, ForceMovement.WEST));
				} else if(ticks == 13) {
					player.setLocation(WALLS[5]);
				} else if(ticks == 14) {
					player.setAnimation(TURN);
					player.getAppearance().setRenderAnimation(RENDER);
					player.addWalkSteps(WALL_END.getX(), WALL_END.getY(), -1, false);
				}
								
				if(player.getLocation().getPositionHash() == WALL_END.getPositionHash() || ticks >= 30) {
					if(ticks < 30) {
						player.getAppearance().resetRenderAnimation();
						player.setFaceLocation(END);
						player.setAnimation(Animation.JUMP);
						player.setForceMovement(new ForceMovement(player.getLocation(), 15, END, 35, ForceMovement.EAST));
						ticks = 30;
					} else {
						player.setLocation(END);
						MarkOfGrace.spawn(player, VarrockRooftopCourse.MARK_LOCATIONS, 40, 30);
						stop();
					}
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 25;
	}

	@Override
	public int getLevel(WorldObject object) {
		return 30;
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 17;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.WALL_14832 };
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START;
	}
	
}
