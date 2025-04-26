package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class YanilleDungeonObstaclePipe implements Shortcut {

    private static final Animation CRAWL = new Animation(749);
    private static final Location MIDDLE = new Location(2575, 9506, 0);
    private static final Location WEST = new Location(2572, 9506, 0);
    private static final Location EAST = new Location(2578, 9506, 0);

    private static final Location easternStartLocation = new Location(2578, 9506, 0);
    private static final Location westernStartLocation = new Location(2572, 9506, 0);

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        if (object.getX() == 2576) {
            return easternStartLocation;
        }
        return westernStartLocation;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final boolean west = object.getRotation() == 0;
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0)
                    player.setAnimation(CRAWL);
                else if(ticks == 1) {
                    player.autoForceMovement(MIDDLE, 90);
                } else if(ticks == 3)
                    player.setLocation(MIDDLE);
                else if(ticks == 5) {
                    player.setAnimation(CRAWL);
                    player.autoForceMovement(west ? EAST : WEST, 90);
                } else if(ticks == 7) {
                    player.setLocation(west ? EAST : WEST);
                    stop();
                }

                ticks++;
            }

        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 49;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.OBSTACLE_PIPE_23140 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 8;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 8;
    }

}