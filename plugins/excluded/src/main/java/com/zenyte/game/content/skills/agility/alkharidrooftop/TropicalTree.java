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
 * @author Tommeh | 26 feb. 2018 : 19:13:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class TropicalTree extends AgilityCourseObstacle {

	private static final Location START_LOC = new Location(3318, 3165, 1);
	private static final Location END_LOC = new Location(3317, 3174, 2);
	private static final Location TREE_LOC = new Location(3318, 3170, 1);
	private static final Location FACE_LOC = new Location(3320, 3170, 1);
	private static final Animation TREE_HANGING_ANIM = new Animation(1122);
	private static final Animation TREE_HANGING_2_ANIM = new Animation(1124);
	private static final Animation JUMP_ANIM = new Animation(2588);

	public TropicalTree() {
		super(AlKharidRooftopCourse.class, 5);
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.faceObject(object);
		player.setForceMovement(new ForceMovement(new Location(player.getLocation()), 55, TREE_LOC, 90, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;

			@Override
			public void run() {
				switch (ticks++) {
				case 1:
					player.setLocation(TREE_LOC);
					break;
				case 2:
					player.setFaceLocation(FACE_LOC);
					player.setAnimation(TREE_HANGING_ANIM);
					break;
				case 3:
					player.setFaceLocation(TREE_LOC);
					player.setAnimation(TREE_HANGING_2_ANIM);
					player.setForceMovement(new ForceMovement(new Location(player.getLocation()), 34, END_LOC, 52, ForceMovement.NORTH));
					break;
				case 4:
					player.setAnimation(JUMP_ANIM);
					player.setLocation(END_LOC);
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
		return new int[] {ObjectId.TROPICAL_TREE_14404 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 10;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 9;
	}

}
