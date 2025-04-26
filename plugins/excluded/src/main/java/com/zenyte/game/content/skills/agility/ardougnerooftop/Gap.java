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
 * @author Tommeh | 08/06/2019 | 11:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class Gap extends AgilityCourseObstacle {

    private static final Animation ANIM1 = new Animation(2586, 15);
    private static final Animation ANIM2 = new Animation(2588);
    private static final Animation ANIM3 = new Animation(2583, 15);

    private static final Location LOCATION1 = new Location(2667, 3311, 1);
    private static final Location LOCATION2 = new Location(2665, 3315, 1);
    private static final Location LOCATION3 = new Location(2665, 3318, 3);

    private static final SoundEffect EFFECT1 = new SoundEffect(2462, 0, 10);
    private static final SoundEffect EFFECT2 = new SoundEffect(2462, 0, 15);

    public Gap() {
        super(ArdougneRooftopCourse.class, 2);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 90;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.GAP_15609 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 6;
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
                        break;
                    case 2:
                        player.setAnimation(ANIM1);
                        player.sendSound(EFFECT2);
                        break;
                    case 3:
                        player.setAnimation(ANIM2);
                        player.setLocation(LOCATION2);
                        break;
                    case 4:
                        player.setAnimation(ANIM3);
                        break;
                    case 5:
                        player.setAnimation(ANIM2);
                        player.setLocation(LOCATION3);
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90);
                        stop();
                        break;
                }

            }
        }, 1, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 65;
    }
}