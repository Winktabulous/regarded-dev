package com.zenyte.game.content.skills.agility.rellekkarooftop;

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
 * @author Tommeh | 09/06/2019 | 14:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class Gap extends AgilityCourseObstacle {

    private static final Animation ANIM1 = new Animation(1995, 15);
    private static final Animation ANIM2 = new Animation(1603);

    private static final Location LOCATION1 = new Location(2622, 3668, 3);
    private static final SoundEffect EFFECT1 = new SoundEffect(1936);

    public Gap() {
        super(RellekkaRooftopCourse.class, 2);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 80;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.GAP_14947 };
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
                        player.sendSound(EFFECT1);
                        player.setAnimation(ANIM2);
                        player.autoForceMovement(LOCATION1, 8, 50);
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
        return 30;
    }
}
