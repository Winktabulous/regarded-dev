package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public final class PickFlowerObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("take-seed")) {
            player.sendMessage("There doesn't seem to be any seeds on this rosebush.");
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROSES_9260, ObjectId.ROSES_9261, ObjectId.ROSES_9262 };
    }
}
