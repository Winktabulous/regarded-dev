/**
 * 
 */
package com.zenyte.game.content.skills.agility.faladorrooftop;

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
 * @author Noele | May 1, 2018 : 1:41:15 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public final class HandHolds extends AgilityCourseObstacle {

	private static final Animation JUMP = new Animation(2583);
	private static final Animation HANG = new Animation(1118);
	private static final Animation LAND = new Animation(1120);
	
	private static final Location[] HOLDS = {
			new Location(3050, 3351, 2),
			new Location(3051, 3352, 2),
			new Location(3051, 3353, 2),
			new Location(3051, 3354, 2),
			new Location(3051, 3355, 2),
			
			new Location(3051, 3351, 2), // special tile (between holds[0] and holds[1])
			new Location(3051, 3356, 2), // special tile (between holds[4] and holds[7])
			new Location(3050, 3357, 3), // holds[7] the end tile
	};
	private static final ForceMovement JUMP_FINISH = new ForceMovement(HOLDS[6], 15, HOLDS[7], 35, ForceMovement.WEST);

	public HandHolds() {
		super(FaladorRooftopCourse.class, 3);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0)
					player.setAnimation(JUMP);
				else if(ticks == 1) {
					player.setAnimation(HANG);
					player.setLocation(HOLDS[0]);
				} else if(ticks == 2)
					player.setForceMovement(new ForceMovement(HOLDS[5], 10, HOLDS[1], 20, ForceMovement.WEST));
				else if(ticks == 3) {
					player.setAnimation(HANG);
					player.setLocation(HOLDS[1]);
				} else if(ticks == 4)
					player.setForceMovement(new ForceMovement(player.getLocation(), 10, HOLDS[2], 20, ForceMovement.WEST));
				else if(ticks == 5) {
					player.setAnimation(HANG);
					player.setLocation(HOLDS[2]);
				} else if(ticks == 6)
					player.setForceMovement(new ForceMovement(player.getLocation(), 10, HOLDS[3], 20, ForceMovement.WEST));
				else if(ticks == 7) {
					player.setAnimation(HANG);
					player.setLocation(HOLDS[3]);
				} else if(ticks == 8)
					player.setForceMovement(new ForceMovement(player.getLocation(), 10, HOLDS[4], 20, ForceMovement.WEST));
				else if(ticks == 9) {
					player.setAnimation(HANG);
					player.setLocation(HOLDS[4]);
				} else if(ticks == 10) {
					player.setAnimation(LAND);
					player.setForceMovement(JUMP_FINISH);
				} else if(ticks == 11) {
					player.setAnimation(Animation.STOP);
					player.setLocation(HOLDS[7]);
					MarkOfGrace.spawn(player, FaladorRooftopCourse.MARK_LOCATIONS, 50, 50);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 50;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 11;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 45;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.HAND_HOLDS_14901 };
	}
}
