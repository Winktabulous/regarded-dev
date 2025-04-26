package com.zenyte.game.content.skills.agility.wildernesscourse;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 18:35:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class LogBalance extends AgilityCourseObstacle {

	private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
	private static final Location START_LOC = new Location(3002, 3945, 0);

	public LogBalance() {
		super(WildernessCourse.class, 4);
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
		player.addWalkSteps(2994, 3945, -1, false);
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 7;
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 52;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23542 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 20;
	}

	@Override
	public RenderAnimation getRenderAnimation() {
		return RENDER;
	}

}
