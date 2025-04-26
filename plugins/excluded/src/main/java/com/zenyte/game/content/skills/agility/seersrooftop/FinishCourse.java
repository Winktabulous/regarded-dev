package com.zenyte.game.content.skills.agility.seersrooftop;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public final class FinishCourse extends AgilityCourseObstacle {

    private static final Location FINISH = new Location(2704, 3464, 0);

    public FinishCourse() {
        super(SeersRooftopCourse.class, 6);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 60;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.EDGE_14931};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 2;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setFaceLocation(FINISH);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;

            @Override
            public void run() {
                if (ticks == 0)
                    player.setAnimation(Animation.LEAP);
                else if (ticks == 1) {
                    player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_LAPS_SEERS_COURSE);
                    player.getAchievementDiaries().update(KandarinDiary.COMPLETE_SEERS_VILLAGE_AGILITY_COURSE_LAP);
                    player.setAnimation(Animation.LAND);
                    player.setLocation(FINISH);
                    MarkOfGrace.spawn(player, SeersRooftopCourse.MARK_LOCATIONS, 60, 20);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 435;
    }
}
