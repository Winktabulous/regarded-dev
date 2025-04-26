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
 * @author Tommeh | 26 feb. 2018 : 17:24:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class Cable extends AgilityCourseObstacle {
	
	private static final Location FIRST_LOC = new Location(3269, 3166, 3);
	private static final Location SECOND_LOC = new Location(3284, 3166, 3);
	private static final Animation RUNUP_ANIM = new Animation(1995);
	private static final Animation ROPESWING_ANIM = new Animation(751);

	public Cable() {
		super(AlKharidRooftopCourse.class, 3);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.addWalkSteps(3266, 3166);
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override
			public void run() {
				switch (ticks++) {
				case 1:
					player.faceObject(object);
					break;
				case 2:
					player.sendFilteredMessage("You begin an almighty run-up...");
					player.setAnimation(RUNUP_ANIM);
					player.setForceMovement(new ForceMovement(new Location(player.getX() + 3, player.getY(), player.getPlane()), 30, ForceMovement.EAST));
					break;
				case 3:
					player.sendFilteredMessage("You gained enough momentum to swing to the other side!");
					player.setAnimation(ROPESWING_ANIM);
					player.setLocation(FIRST_LOC);
					player.setForceMovement(new ForceMovement(new Location(player.getX() + 15, player.getY(), player.getPlane()), 60, ForceMovement.EAST));
					break;
				case 4:
					player.setLocation(SECOND_LOC);
					MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20);
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
		return new int[] {ObjectId.CABLE };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 40;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 9;
	}

}
