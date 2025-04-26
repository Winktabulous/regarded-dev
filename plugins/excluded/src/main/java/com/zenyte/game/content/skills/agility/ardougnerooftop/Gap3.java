package com.zenyte.game.content.skills.agility.ardougnerooftop;

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
 * @author Tommeh | 08/06/2019 | 12:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class Gap3 extends AgilityCourseObstacle {

    private static final Animation ANIM1 = new Animation(7133, 15);
    private static final Animation ANIM2 = new Animation(2588);

    private static final Location LOCATION1 = new Location(2651, 3309, 3);
    private static final SoundEffect EFFECT1 = new SoundEffect(2462, 0, 15);

    public Gap3() {
        super(ArdougneRooftopCourse.class, 5);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 90;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.GAP_15611 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 3;
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
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90);
                        stop();
                        break;
                }

            }
        }, 1, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 28;
    }
}