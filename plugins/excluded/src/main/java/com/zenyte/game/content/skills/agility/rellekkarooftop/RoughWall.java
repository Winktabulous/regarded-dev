package com.zenyte.game.content.skills.agility.rellekkarooftop;

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
 * @author Tommeh | 09/06/2019 | 11:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class RoughWall extends AgilityCourseObstacle {

    private static final Animation ANIM1 = new Animation(828, 15);

    private static final Location LOCATION1 = new Location(2626, 3676, 3);

    public RoughWall() {
        super(RellekkaRooftopCourse.class, 1);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 80;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.ROUGH_WALL_14946 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 3;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;

            @Override
            public void run() {
                switch (ticks++) {
                    case 0:
                        player.setAnimation(ANIM1);
                        break;
                    case 2:
                        player.setLocation(LOCATION1);
                        player.setAnimation(Animation.STOP);
                        MarkOfGrace.spawn(player, RellekkaRooftopCourse.MARK_LOCATIONS, 40, 80);
                        stop();
                        break;
                }

            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 20;
    }
}