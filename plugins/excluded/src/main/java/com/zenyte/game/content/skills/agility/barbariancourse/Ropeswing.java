package com.zenyte.game.content.skills.agility.barbariancourse;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 9. veebr 2018 : 4:50.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Ropeswing extends AgilityCourseObstacle {

	private static final Location START_LOC = new Location(2551, 3554, 0);
	private static final Animation SWINGING_ANIM = new Animation(751);
	private static final Animation ROPE_ANIM = new Animation(497);
	private static final ForceMovement FORCE_MOVEMENT = new ForceMovement(new Location(2551, 3553, 0), 30, new Location(2551, 3549, 0), 60, ForceMovement.SOUTH);

	public Ropeswing() {
		super(BarbarianOutpostCourse.class, 1);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.setAnimation(SWINGING_ANIM);
		World.sendObjectAnimation(object, ROPE_ANIM);
		player.setFaceLocation(FORCE_MOVEMENT.getToFirstTile());
		player.setForceMovement(FORCE_MOVEMENT);
		WorldTasksManager.schedule(() -> player.setLocation(FORCE_MOVEMENT.getToSecondTile()), 1);
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 35;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23131 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 22;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 2;
	}

}
