package com.zenyte.game.content.skills.agility.draynorrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 22:44:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class Gap extends AgilityCourseObstacle {

	private static final Location START_LOC = new Location(3094, 3255, 3);
	private static final Location END_LOC = new Location(3096, 3256, 3);
	private static final Animation FIRST_ANIM = new Animation(2586, 15);
	private static final Animation SECOND_ANIM = new Animation(2588);

	public Gap() {
		super(DraynorRooftopCourse.class, 6);
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.setAnimation(FIRST_ANIM);
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override
			public void run() {
				switch (ticks++) {
				case 0:
					player.setLocation(END_LOC);
					player.setAnimation(SECOND_ANIM);
					break;
				case 1:
					player.setAnimation(Animation.STOP);
					MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10);
					stop();
					break;
				}
			}
		}, 0, 1);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 10;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.GAP_11631 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 4;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 1;
	}

}
