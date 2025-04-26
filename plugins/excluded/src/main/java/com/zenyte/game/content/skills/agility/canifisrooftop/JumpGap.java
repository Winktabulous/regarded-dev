package com.zenyte.game.content.skills.agility.canifisrooftop;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
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
 * @author Noele | Apr 29, 2018 : 9:50:21 AM
 * @see <a href="https://noeles.life">|| noele@zenyte.com</a>
 */
public final class JumpGap extends AgilityCourseObstacle {

    private static final Animation JUMP = new Animation(2586);

    private static final Animation LAND = new Animation(2588);

    public JumpGap() {
        super(CanifisRooftopCourse.class, 2);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final GapInfo gap = GapInfo.get(object.getId());
        if (gap == null) {
            return;
        }
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(JUMP);
                } else if (ticks == 1) {
                    if (gap.equals(GapInfo.FIFTH)) {
                        player.getAchievementDiaries().update(MorytaniaDiary.COMPLETE_CANIFIS_COURSE_LAP);
                    }
                    player.setAnimation(LAND);
                    player.setLocation(gap.getFinish());
                    MarkOfGrace.spawn(player, CanifisRooftopCourse.MARK_LOCATIONS, 40, 40);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 40;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { ObjectId.GAP_14844, ObjectId.GAP_14845, ObjectId.GAP_14846, ObjectId.GAP_14847, ObjectId.GAP_14897 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        if (object.getId() == ObjectId.GAP_14897)
            return 175;
        return object.getId() == 10823 ? 11 : 8;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return GapInfo.get(object.getId()) == null ? object : GapInfo.get(object.getId()).getStart();
    }
}
