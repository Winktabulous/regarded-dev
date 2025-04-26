package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 15/04/2019 21:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LighthouseDoor implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Location destination = player.getY() <= 3635 ? new Location(2509, 3636, 0) : new Location(2509, 3635, 0);
        player.lock(3);
        final WorldObject obj = new WorldObject(object);
        obj.setId(4578);
        World.spawnObject(obj);
        player.addWalkSteps(destination.getX(), destination.getY(), 1, false);
        WorldTasksManager.schedule(() -> World.spawnObject(object), 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOORWAY_4577 };
    }
}
