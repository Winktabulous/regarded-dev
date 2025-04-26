package com.zenyte.game.content.skills.agility.wildernesscourse;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 18:35:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class Rocks extends AgilityCourseObstacle {

	private static final Animation CLIMBING_ANIM = new Animation(740);

	public Rocks() {
		super(WildernessCourse.class, 5);
	}

	@Override
	public String getEndMessage(final boolean success) {
		return "You reach the top.";
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.setAnimation(CLIMBING_ANIM);
		player.setForceMovement(new ForceMovement(new Location(object.getX(), object.getY() - 3, object.getPlane()), 120, ForceMovement.SOUTH));
		WorldTasksManager.schedule(() -> {
			player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_LAPS_WILDERNESS_AGILITY_COURSE);
			player.getAchievementDiaries().update(WildernessDiary.COMPLETE_AGILITY_COURSE_LAP);
			player.setAnimation(Animation.STOP);
			player.setLocation(new Location(object.getX(), object.getY() - 3, object.getPlane()));
		}, 3);
	}


	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 52;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23640 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 0;
	}

}
