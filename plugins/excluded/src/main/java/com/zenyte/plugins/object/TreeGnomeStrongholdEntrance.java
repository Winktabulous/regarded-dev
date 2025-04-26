package com.zenyte.plugins.object;

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
 * @author Kris | 7. juuni 2018 : 01:54:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class TreeGnomeStrongholdEntrance implements ObjectAction {

    private static final Location OUTSIDE_LOC = new Location(2461, 3382, 0);

    private static final Location INSIDE_LOC = new Location(2461, 3385, 0);

    private static final WorldObject LEFT_GATE = new WorldObject(191, 10, 2, 2459, 3383, 0);

    private static final WorldObject RIGHT_GATE = new WorldObject(192, 10, 0, 2462, 3383, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Location destination = player.inArea("Tree Gnome Stronghold") ? OUTSIDE_LOC : INSIDE_LOC;
        player.lock();
        World.removeGraphicalDoor(object);
        World.spawnGraphicalDoor(LEFT_GATE);
        World.spawnGraphicalDoor(RIGHT_GATE);
        player.setRunSilent(3);
        player.addWalkSteps(destination.getX(), destination.getY(), -1, false);
        WorldTasksManager.schedule(() -> {
            World.removeGraphicalDoor(LEFT_GATE);
            World.removeGraphicalDoor(RIGHT_GATE);
            World.spawnGraphicalDoor(object);
            player.unlock();
        }, 3);
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(!player.inArea("Tree Gnome Stronghold") ? OUTSIDE_LOC : INSIDE_LOC), () -> {
            if (World.getObjectWithId(object, object.getId()) == null || player.getPlane() != object.getPlane()) {
                return;
            }
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        }, getDelay()));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_190 };
    }
}
