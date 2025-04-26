package com.zenyte.game.content.skills.agility.barbariancourse;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 24 feb. 2018 : 19:58:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class BalancingLedge extends AgilityCourseObstacle {

	private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 756, RenderAnimation.WALK);
	private static final Location START_LOC = new Location(2536, 3547, 1);

	public BalancingLedge() {
		super(BarbarianOutpostCourse.class, 4);
	}

	@Override
	public String getStartMessage(final boolean success) {
		return "You put your foot on the ledge and try to edge across...";
	}

	@Override
	public String getEndMessage(final boolean success) {
		return "You skillfully edge across the gap.";
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.addWalkSteps(2532, 3547, -1, false);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 35;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23547 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 22;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}
	
	@Override
	public RenderAnimation getRenderAnimation() {
		return RENDER;
	}

}
