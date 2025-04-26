package com.zenyte.game.content.skills.agility.barbariancourse;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 9. veebr 2018 : 5:13.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ObstacleNet extends AgilityCourseObstacle {

	private static final Location SOUTHERN_START_LOC = new Location(2539, 3545, 0);
	private static final Location NORTHERN_START_LOC = new Location(2539, 3546, 0);
	
	private static final Location SOUTHERN_END_LOC = new Location(2537, 3545, 1);
	private static final Location NORTHERN_END_LOC = new Location(2537, 3546, 1);

	public ObstacleNet() {
		super(BarbarianOutpostCourse.class, 3);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.useStairs(828, player.getY() <= 3545 ? SOUTHERN_END_LOC : NORTHERN_END_LOC, 1, 2);
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return player.getY() <= 3545 ? SOUTHERN_START_LOC : NORTHERN_START_LOC;
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 35;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 20211 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 8.2;
	}

	@Override
	public String getStartMessage(final boolean success) {
		return "You climb the obstacle net...";
	}

	@Override
	public String getEndMessage(final boolean success) {
		return "...to the platform above.";
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 1;
	}

}
