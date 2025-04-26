package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 31 aug. 2018 | 20:46:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TutorialIslandHouseDoorObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.sendMessage("You cannot leave through this door!");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_9398 };
    }
}
