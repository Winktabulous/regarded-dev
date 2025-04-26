package com.zenyte.game.content.skills.agility.alkharidrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 26 feb. 2018 : 21:06:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class RooftopBeams extends AgilityCourseObstacle {
	
	private static final Location END_LOC = new Location(3316, 3180, 3);

	public RooftopBeams() {
		super(AlKharidRooftopCourse.class, 6);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20);
		player.useStairs(828, END_LOC, 1, 1);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 20;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.ROOF_TOP_BEAMS };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 5;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 1;
	}

}
