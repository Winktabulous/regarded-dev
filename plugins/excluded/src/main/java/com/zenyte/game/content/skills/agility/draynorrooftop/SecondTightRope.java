package com.zenyte.game.content.skills.agility.draynorrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 21:41:13
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class SecondTightRope extends AgilityCourseObstacle {

	private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
	private static final Location START_LOC = new Location(3091, 3276, 3);

	public SecondTightRope() {
		super(DraynorRooftopCourse.class, 3);
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10);
		player.addWalkSteps(3092, 3276, -1, false);
		player.addWalkSteps(3092, 3267, -1, false);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 10;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.TIGHTROPE_11406 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 8;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 9;
	}

	@Override
	public RenderAnimation getRenderAnimation() {
		return RENDER;
	}

}
