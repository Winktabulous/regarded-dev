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

public final class ClothesLine extends AgilityCourseObstacle {
		
	private static final Location FIRST_JUMP = new Location(3212, 3414, 3);
	private static final Location SECOND_JUMP = new Location(3210, 3414, 3);
	private static final Location THIRD_JUMP = new Location(3208, 3414, 3);

	public ClothesLine() {
		super(VarrockRooftopCourse.class, 2);
	}

	@Override
	public void startSuccess(Player player, WorldObject object) {
		player.faceObject(object);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, FIRST_JUMP, 35, ForceMovement.WEST));
				} else if (ticks == 1)
					player.setLocation(FIRST_JUMP);
				else if (ticks == 2) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, SECOND_JUMP, 35, ForceMovement.WEST));
				} else if (ticks == 3)
					player.setLocation(SECOND_JUMP);
				else if (ticks == 4) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, THIRD_JUMP, 35, ForceMovement.WEST));
				} else if (ticks == 5) {
					player.setLocation(THIRD_JUMP);
					MarkOfGrace.spawn(player, VarrockRooftopCourse.MARK_LOCATIONS, 40, 30);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 21;
	}

	@Override
	public int getLevel(WorldObject object) {
		return 30;
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 5;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.CLOTHES_LINE };
	}

}
