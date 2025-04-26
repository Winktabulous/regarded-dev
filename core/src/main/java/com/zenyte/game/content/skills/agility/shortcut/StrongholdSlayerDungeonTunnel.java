package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StrongholdSlayerDungeonTunnel implements Shortcut {
    private static final Animation CRAWL = new Animation(749);
    private static final Location MIDDLE = new Location(2432, 9806, 0);
    private static final Location WEST = new Location(2429, 9806, 0);
    private static final Location EAST = new Location(2435, 9806, 0);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final Location west = new Location(WEST.getX(), player.getY(), player.getPlane());
        final Location middle = new Location(MIDDLE.getX(), player.getY(), player.getPlane());
        final Location east = new Location(EAST.getX(), player.getY(), player.getPlane());
        final boolean isWest = player.getX() <= WEST.getX();
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(CRAWL);
                } else if (ticks == 1) {
                    player.setForceMovement(new ForceMovement(middle, 90, isWest ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 2) {
                    player.getAppearance().setInvisible(true);
                } else if (ticks == 3) {
                    player.setLocation(middle);
                } else if (ticks == 5) {
                    player.setAnimation(CRAWL);
                    player.setForceMovement(new ForceMovement(isWest ? east : west, 90, isWest ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 6) {
                    player.getAppearance().setInvisible(false);
                } else if (ticks == 7) {
                    player.setLocation(isWest ? east : west);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 72;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {30174, 30175};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 5;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
