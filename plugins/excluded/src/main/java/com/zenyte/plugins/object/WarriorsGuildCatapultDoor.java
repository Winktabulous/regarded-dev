package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 16. dets 2017 : 1:49.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WarriorsGuildCatapultDoor implements ObjectAction {

    private static final Location LOCATION = new Location(2842, 3541, 1);

    private static final Location OUTSIDE = new Location(2842, 3542, 1);

    private static final WorldObject DOOR = new WorldObject(24312, 0, 0, OUTSIDE);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        handleDoor(player, object);
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(player.getY() >= 3542 ? OUTSIDE : LOCATION), () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        }));
    }

    private void handleDoor(final Player player, final WorldObject object) {
        if (object.isLocked()) {
            return;
        }
        player.setFaceLocation(player.getLocation().getPositionHash() == DOOR.getPositionHash() ? LOCATION : DOOR);
        if (player.getLocation().getPositionHash() == DOOR.getPositionHash()) {
            handle(player, object, LOCATION);
        } else {
            handle(player, object, DOOR);
        }
    }

    private final void handle(final Player player, final WorldObject object, final Location tile) {
        object.setLocked(true);
        player.lock();
        World.spawnGraphicalDoor(DOOR);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.addWalkSteps(tile.getX(), tile.getY(), 1, false);
                } else if (ticks == 1) {
                    player.unlock();
                    World.spawnGraphicalDoor(World.getObjectWithType(DOOR, 0));
                } else if (ticks == 2) {
                    object.setLocked(false);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_24312 };
    }
}
