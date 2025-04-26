package com.zenyte.game.content.skills.agility.alkharidrooftop;

import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
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

	private static final Location START_LOC = new Location(3300, 3192, 3);
	private static final Location END_LOC = new Location(3299, 3194, 0);
	private static final Animation JUMP_ANIM = new Animation(2586);

	public Gap() {
		super(AlKharidRooftopCourse.class, 8);
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.setAnimation(JUMP_ANIM);
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override
			public void run() {
				switch (ticks++) {
				case 0:
					player.setLocation(END_LOC);
					break;
				case 1:
					player.getAchievementDiaries().update(LumbridgeDiary.COMPLETE_ALKHARID_COURSE);
					player.setAnimation(Animation.STOP);
					MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20);
					stop();
					break;
				}
			}
		}, 0, 1);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 20;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.GAP_14399 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 30;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 1;
	}

}
