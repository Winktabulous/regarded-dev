package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class MisthalinSteppingStone implements Shortcut {

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.setFaceLocation(object);
        final int faceDirection = DirectionUtil.getFaceDirection(object.getX() - player.getX(), object.getY() - player.getY());
        final Location destination = getStartPosition(player, object).transform(Direction.values[((faceDirection + 128) & 2047) >> 8], 4);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, faceDirection));
                } else if (ticks == 1) {
                    player.setLocation(object);
                } else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, destination, 35, faceDirection));
                } else if (ticks == 3) {
                    player.setLocation(destination);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 5948, 5949 };
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
        return getStartPosition(player, object);
    }

    private Location getStartPosition(final Player player, final WorldObject object) {
        if (object.getId() == ObjectId.STEPPING_STONE_5949) {
            final Location north = new Location(3221, 9556);
            return player.getY() >= 9555 ? north : north.transform(0, -4, 0);
        } else {
            final Location east = new Location(3208, 9572);
            return player.getX() >= 3207 ? east : east.transform(-4, 0, 0);
        }
    }
}
