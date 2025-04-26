package com.zenyte.game.content.skills.agility.gnomecourse;

import com.zenyte.game.content.skills.agility.AgilityCourse;
import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 21. dets 2017 : 18:25.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BalancingRope extends AgilityCourseObstacle {

	private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);

	public BalancingRope() {
		super(GnomeCourse.class, 4);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.addWalkSteps(2483, 3420, -1, false);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 1;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23557 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 7.5;
	}

	@Override
	public String getStartMessage(final boolean success) {
		return "You carefully cross the tightrope.";
	}

	@Override
	public RenderAnimation getRenderAnimation() {
		return RENDER;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 7;
	}

	@Override
	public Class<? extends AgilityCourse> getCourse() {
		return GnomeCourse.class;
	}

}
