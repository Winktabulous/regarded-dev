package com.zenyte.game.content.skills.agility.ardougnerooftop;

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
 * @author Kris | 08/06/2019 09:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class WoodenBeams extends AgilityCourseObstacle {

    private static final Animation ANIM1 = new Animation(737, 15);
    private static final Animation ANIM2 = new Animation(737);
    private static final Animation ANIM3 = new Animation(2588);

    private static final Location LOCATION1 = new Location(2673, 3298, 1);
    private static final Location LOCATION2 = new Location(2673, 3298, 2);
    private static final Location LOCATION3 = new Location(2671, 3299, 3);

    public WoodenBeams() {
        super(ArdougneRooftopCourse.class, 1);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 90;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.WOODEN_BEAMS };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 5;
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
                        break;
                    case 1:
                        player.setLocation(LOCATION1);
                        player.setAnimation(ANIM2);
                        break;
                    case 2:
                        player.setLocation(LOCATION2);
                        player.setAnimation(ANIM2);
                        break;
                    case 3:
                        player.setLocation(LOCATION3);
                        player.setAnimation(ANIM3);
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90);
                        stop();
                        break;
                }

            }
        }, 1, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 43;
    }
}
