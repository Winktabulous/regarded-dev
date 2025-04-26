package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Chris
 * @since June 09 2020
 */
public class PollnivneachBrokenLadder implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.sendMessage("This ladder seems to be broken.");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.LADDER_17028};
    }
}
