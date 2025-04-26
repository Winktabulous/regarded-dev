package com.zenyte.game.content.skills.agility.barbariancourse;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 24 feb. 2018 : 22:01:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class CrumblingWall extends AgilityCourseObstacle {
	private static final Location WESTERN_START = new Location(2535, 3553, 0);
	private static final Location MIDDLE_START = new Location(2538, 3553, 0);
	private static final Location EASTERN_START = new Location(2541, 3553, 0);
	private static final Animation ANIM = new Animation(839, 15);

	public CrumblingWall() {
		super(BarbarianOutpostCourse.class, 5);
	}

	@Override
	public boolean preconditions(final Player player, final WorldObject object) {
		if (player.getX() >= object.getX()) {
			player.sendMessage("You can\'t climb over the wall from here.");
			return false;
		}
		return true;
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final Location destination = new Location(object.getX() + 1, object.getY(), object.getPlane());
		player.setAnimation(ANIM);
		player.setForceMovement(new ForceMovement(destination, 90, ForceMovement.EAST));
		WorldTasksManager.schedule(() -> {
			if (destination.getX() == 2543) {
				player.getAchievementDiaries().update(KandarinDiary.COMPLETE_BARBARIAN_AGILITY_COURSE_LAP);
			}
			player.setLocation(destination);
		}, 2);
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		if (object.matches(new Location(2536, 3553, 0))) {
			return WESTERN_START;
		} else if (object.matches(new Location(2539, 3553, 0))) {
			return MIDDLE_START;
		}
		return EASTERN_START;
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 35;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {1948};
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 13.7;
	}

	@Override
	public String getStartMessage(final boolean success) {
		return "You climb the low wall...";
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 3;
	}
}
