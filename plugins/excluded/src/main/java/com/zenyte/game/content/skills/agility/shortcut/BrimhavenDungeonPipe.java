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
 * @author Kris | 24/04/2019 16:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BrimhavenDungeonPipe implements Shortcut {

    private static final Animation CRAWL = new Animation(749);
    private static final Location MIDDLE = new Location(2698, 9496, 0);
    private static final Location NORTH = new Location(2698, 9500, 0);
    private static final Location SOUTH = new Location(2698, 9492, 0);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final boolean south = object.getRotation() == 3;
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0)
                    player.setAnimation(CRAWL);
                else if(ticks == 1) {
                    player.setForceMovement(new ForceMovement(MIDDLE, 90, south ? ForceMovement.NORTH : ForceMovement.SOUTH));
                } else if(ticks == 3)
                    player.setLocation(MIDDLE);
                else if(ticks == 5) {
                    player.setAnimation(CRAWL);
                    player.setForceMovement(new ForceMovement(south ? NORTH : SOUTH, 90, south ? ForceMovement.NORTH : ForceMovement.SOUTH));
                } else if(ticks == 7) {
                    player.setLocation(south ? NORTH : SOUTH);
                    stop();
                }

                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 1;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 21727 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 7;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
