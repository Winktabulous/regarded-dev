package com.zenyte.game.content.skills.agility.priffdinasrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class ThirdTightRope extends AgilityCourseObstacle {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.RUN);
    private static final Location VALID_LOC = new Location(3268, 6161, 2);
    private static final Location START_LOC = new Location(3268, 6162, 2);
    private static final Location END_LOC = new Location(3274, 6168, 2);
    private static final int WALK_TIME = 7;
    private static final SoundEffect TIGHT_ROPE_SOUND = new SoundEffect(2495);

    public ThirdTightRope() {
        super(PriffdinasRooftopCourse.class, 11);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return VALID_LOC;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (player.getNumericTemporaryAttribute("prif_portal").intValue() == 6) {
            player.getVarManager().sendBit(9298, 6);
        }
        player.getPacketDispatcher().sendSoundEffect(TIGHT_ROPE_SOUND);
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                    case 0: {
                        player.addWalkSteps(START_LOC.getX(), START_LOC.getY(), -1, false);
                        break;
                    }
                    case 1: {
                        player.setFaceLocation(END_LOC);
                        player.addWalkSteps(END_LOC.getX(), END_LOC.getY(), -1, false);
                        stop();
                        break;
                    }
                }
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 36236 };
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 30.7;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return WALK_TIME;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

}