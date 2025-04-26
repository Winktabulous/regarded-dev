package com.zenyte.game.content.skills.agility.draynorrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 20:15:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class RoughWall extends AgilityCourseObstacle {
	
	private static final Location END_LOC = new Location(3102, 3279, 3);

	public RoughWall() {
		super(DraynorRooftopCourse.class, 1);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10);
		player.useStairs(828, END_LOC, 1, 1);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 10;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.ROUGH_WALL };
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
