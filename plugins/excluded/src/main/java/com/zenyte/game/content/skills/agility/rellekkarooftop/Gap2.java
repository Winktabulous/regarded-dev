package com.zenyte.game.content.skills.agility.rellekkarooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 09/06/2019 | 15:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class Gap2 extends AgilityCourseObstacle {

    private static final RenderAnimation RENDER1 = new RenderAnimation(755, 755, 754, 754, 754, 754, -1);
    private static final RenderAnimation RENDER2 = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);


    private static final Animation ANIM1 = new Animation(752);

    public Gap2() {
        super(RellekkaRooftopCourse.class, 4);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 80;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.GAP_14990 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 14;
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
                        player.autoForceMovement(player.getLocation().transform(0, 3, 0), 30, 25);
                        break;
                    case 1:
                        player.setAnimation(ANIM1);
                        player.getAppearance().setRenderAnimation(RENDER1);
                        break;
                    case 2:
                        player.addWalkSteps(2635, 3658, -1, false);
                        break;
                    case 8:
                        player.getAppearance().setRenderAnimation(RENDER2);
                        player.addWalkSteps(2640, 3653, -1, false);
                        break;
                    case 14:
                        player.getAppearance().resetRenderAnimation();
                        MarkOfGrace.spawn(player, RellekkaRooftopCourse.MARK_LOCATIONS, 40, 80);
                        stop();
                        break;
                }

            }
        }, 1, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 85;
    }
}
