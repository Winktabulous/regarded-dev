package com.zenyte.game.content.skills.agility.priffdinasrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class FirstDarkHole extends AgilityCourseObstacle {

    private static final Location START_LOC = new Location(3269, 6117, 0);
    private static final Location END_LOC = new Location(3293, 6141, 0);
    private static final Animation ENTER_HOLE = new Animation(827);

    public FirstDarkHole() {
        super(PriffdinasRooftopCourse.class, 5);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{36229};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 5;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.forceAnimation(ENTER_HOLE);
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            final FadeScreen fs = new FadeScreen(player);
            @Override
            public void run() {
                switch (ticks++) {
                    case 0: {
                        fs.fade(4);
                        break;
                    }
                    case 2: {
                        player.teleport(END_LOC);
                        break;
                    }
                    case 5: {
                        fs.unfade();
                        stop();
                        break;
                    }
                }
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 11.5;
    }
}