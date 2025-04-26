package com.zenyte.game.content.pyramidplunder.object;

import com.zenyte.game.content.pyramidplunder.PyramidPlunderConstants;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class PlunderLobbyExit implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock(1);
        player.setLocation(PyramidPlunderConstants.OUTSIDE_PYRAMID);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.TOMB_DOOR_20932};
    }
}
