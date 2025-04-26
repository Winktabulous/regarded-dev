package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class ZulandraSteppingStone implements Shortcut {

    private static final Location WEST = new Location(2154, 3072, 0);
    private static final Location EAST = new Location(2160, 3072, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final boolean west = player.getX() < object.getX();
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if(ticks == 1)
                    player.setLocation(object);
                else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, west ? EAST : WEST, 35, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if(ticks == 3) {
                    player.setLocation(west ? EAST : WEST);
                    stop();
                }

                ticks++;
            }


        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 76;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 10663 };
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
        return player.getX() < object.getX() ? WEST : EAST;
    }
}
