package com.zenyte.game.content.skills.agility.gnomecourse;

import com.zenyte.game.content.skills.agility.AgilityCourse;
import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 21. dets 2017 : 18:28.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class UpperTreeBranch extends AgilityCourseObstacle {

	public UpperTreeBranch() {
		super(GnomeCourse.class, 5);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.useStairs(828, new Location(2487, 3420, 0), 1, 2);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 1;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23560 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 5;
	}

	@Override
	public String getEndMessage(final boolean success) {
		return "You land on the ground.";
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 1;
	}

	@Override
	public Class<? extends AgilityCourse> getCourse() {
		return GnomeCourse.class;
	}

}
