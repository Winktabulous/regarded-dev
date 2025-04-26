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
 * @author Kris | 10/05/2019 17:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZeahHosidiusSteppingStone implements Shortcut {

    private static final Location WEST = new Location(1720, 3551, 0);
    private static final Location EAST = new Location(1724, 3551, 0);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean direction = player.getX() >= 1724;
        player.setFaceLocation(object);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction ? ForceMovement.WEST : ForceMovement.EAST));
                } else if(ticks == 1) {
                    player.setLocation(object);
                } else if(ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, direction ? WEST : EAST, 35, direction ? ForceMovement.WEST : ForceMovement.EAST));
                } else if (ticks == 3) {
                    player.setLocation(direction ? WEST : EAST);
                    stop();
                }

                ticks++;
            }

        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 45;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 29728 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return player.inArea("Great Kourend: Hosidius House") ? EAST : WEST;
    }

}
