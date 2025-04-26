package com.zenyte.game.content.skills.agility.rellekkarooftop;

import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 09/06/2019 | 15:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class PileOfFish extends AgilityCourseObstacle {

    private static final Animation ANIM1 = new Animation(2586, 15);
    private static final Animation ANIM2 = new Animation(2588);

    private static final Location LOCATION1 = new Location(2653, 3676, 0);
    private static final SoundEffect EFFECT1 = new SoundEffect(2462, 0, 15);

    public PileOfFish() {
        super(RellekkaRooftopCourse.class, 7);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 80;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.PILE_OF_FISH };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.setFaceLocation(object);
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                    case 0:
                        player.setAnimation(ANIM1);
                        player.sendSound(EFFECT1);
                        break;
                    case 1:
                        player.setAnimation(ANIM2);
                        player.setLocation(LOCATION1);
                        player.getAchievementDiaries().update(FremennikDiary.COMPLETE_RELLEKKA_AGILITY_COURSE_LAP);
                    case 2:
                        player.addWalkSteps(2652, 3676, -1, false);
                        break;
                    case 3:
                        MarkOfGrace.spawn(player, RellekkaRooftopCourse.MARK_LOCATIONS, 40, 80);
                        stop();
                        break;
                }

            }
        }, 1, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 475;
    }
}
