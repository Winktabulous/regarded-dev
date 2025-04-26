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

public final class HurdleLedge extends AgilityCourseObstacle {
	
	private static final Animation JUMP = new Animation(1603);

	public HurdleLedge() {
		super(VarrockRooftopCourse.class, 7);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final Location finish = new Location(player.getX(), player.getY()+2, 3);
		player.setFaceLocation(finish);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, finish, 35, ForceMovement.NORTH));
				} else if(ticks == 1) {
					player.setLocation(finish);
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
		return 2;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 3;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.LEDGE_14836 };
	}
}
