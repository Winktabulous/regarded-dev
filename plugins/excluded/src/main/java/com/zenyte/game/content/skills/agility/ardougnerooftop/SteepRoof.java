package com.zenyte.game.content.skills.agility.ardougnerooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 08/06/2019 | 12:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class SteepRoof extends AgilityCourseObstacle {

    private static final Animation ANIM1 = new Animation(753);
    private static final Animation ANIM2 = new Animation(759);

    private static final RenderAnimation RENDER = new RenderAnimation(757, 757, 756, 756, 756, 756, -1);

    public SteepRoof() {
        super(ArdougneRooftopCourse.class, 6);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 90;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.STEEP_ROOF };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 6;
    }

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (player.getLocation().matches(new Location(2654, 3299, 3))) {
            player.sendMessage("You can't go back from here.");
            return false;
        }
        return true;
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
                        player.getAppearance().setRenderAnimation(RENDER);
                        break;
                    case 1:
                        player.autoForceMovement(new Location(2654, 3299, 3), 30);
                        break;
                    case 2:
                        player.autoForceMovement(new Location(2656, 3297, 3), 30);
                        break;
                    case 5:
                        player.addWalkSteps(2656, 3295, 3);
                        player.getAppearance().resetRenderAnimation();
                        player.setAnimation(ANIM2);
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90);
                        stop();
                        break;
                }

            }
        }, 1, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 57;
    }

}
