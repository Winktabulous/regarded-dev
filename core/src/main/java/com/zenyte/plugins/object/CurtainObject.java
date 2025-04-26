package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 11/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CurtainObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Open") || option.equalsIgnoreCase("Close")) {
            final WorldObject replacementObject = new WorldObject(object);
            replacementObject.setId(option.equalsIgnoreCase("Close") ? 1533 : 1534);
            World.spawnObject(replacementObject);
            if (!World.isSpawnedObject(object)) {
                WorldTasksManager.schedule(() -> {
                    if (World.getObjectWithType(replacementObject, replacementObject.getType()) == replacementObject) {
                        World.spawnObject(object);
                    }
                }, 500);
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CURTAIN_1533, ObjectId.CURTAIN_1534 };
    }
}
