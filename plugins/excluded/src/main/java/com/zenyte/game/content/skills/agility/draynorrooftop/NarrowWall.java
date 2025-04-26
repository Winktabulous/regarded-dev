package com.zenyte.game.content.skills.agility.draynorrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 22:29:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class NarrowWall extends AgilityCourseObstacle {

	private static final RenderAnimation RENDER = new RenderAnimation(757, 757, 756, 756, 756, 756, -1);
	private static final Location START_LOC = new Location(3089, 3265, 3);

	public NarrowWall() {
		super(DraynorRooftopCourse.class, 4);
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10);
		player.setAnimation(new Animation(753));
		player.addWalkSteps(3089, 3262, -1, false);
		player.addWalkSteps(3088, 3261, -1, false);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 10;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.NARROW_WALL };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 7;
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
