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
 * @author Kris | 10/05/2019 19:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MythsCaveDungeonPillar implements Shortcut {
    private static final Location NORTH = new Location(1981, 8999, 1);
    private static final Location SOUTH = new Location(1981, 8994, 1);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final boolean south = player.getY() < object.getY();
        final Location dest = south ? NORTH : SOUTH;
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, !south ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if (ticks == 1) player.setLocation(object);
                 else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, dest, 35, !south ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if (ticks == 3) {
                    player.setLocation(dest);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 15;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {31809};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }

    @Override
    public Location getRouteEvent(Player player, WorldObject object) {
        return player.getY() > object.getY() ? NORTH : SOUTH;
    }
}
