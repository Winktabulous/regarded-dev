package com.zenyte.game.content.skills.agility.gnomecourse;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 21. dets 2017 : 17:42.09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LogBalance extends AgilityCourseObstacle {

	private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);

	public LogBalance() {
		super(GnomeCourse.class, 1);
	}

	@Override
	public String getStartMessage(final boolean success) {
		return "You walk carefully across the slippery log...";
	}

	@Override
	public String getEndMessage(final boolean success) {
		return "...You make it safely to the other side.";
	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.addWalkSteps(2474, 3429, -1, false);
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 7;
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 1;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23145 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 7.5;
	}

	@Override
	public RenderAnimation getRenderAnimation() {
		return RENDER;
	}

}
