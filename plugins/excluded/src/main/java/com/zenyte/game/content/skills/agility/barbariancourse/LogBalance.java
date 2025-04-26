package com.zenyte.game.content.skills.agility.barbariancourse;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 9. veebr 2018 : 5:07.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LogBalance extends AgilityCourseObstacle {

	private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
	private static final Location START_LOC = new Location(2551, 3546, 0);

	public LogBalance() {
		super(BarbarianOutpostCourse.class, 2);
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
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.addWalkSteps(2541, 3546, -1, false);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 35;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23144 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 13.7;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 10;
	}
	
	@Override
	public RenderAnimation getRenderAnimation() {
		return RENDER;
	}

}
