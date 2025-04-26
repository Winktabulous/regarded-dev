package com.zenyte.game.content.skills.agility.priffdinasrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class RoofEdge extends AgilityCourseObstacle {

    private static final Location START_LOC = new Location(3269, 6115, 2);
    private static final Location END_LOC = new Location(3269, 6117, 0);
    private static final Animation YEET_JUMP = new Animation(2586);
    private static final Animation YEET_LANDING = new Animation(2588);

    public RoofEdge() {
        super(PriffdinasRooftopCourse.class, 4);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 75;
    }

    @Override
    public int distance(@NotNull final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{36228};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 2;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (player.getNumericTemporaryAttribute("prif_portal").intValue() == 2) {
            player.getVarManager().sendBit(9298, 2);
        }
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                    case 0: {
                        player.forceAnimation(YEET_JUMP);
                        break;
                    }
                    case 1: {
                        player.forceAnimation(YEET_LANDING);
                        player.teleport(END_LOC);
                        stop();
                        break;
                    }
                }
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 23.0;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }
}