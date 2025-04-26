package com.zenyte.game.content.tog;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Chris
 * @since September 08 2020
 */
public class WeepingWall implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final TearsOfGuthixWall togWall = TearsOfGuthixWall.of(object.getPosition());
        player.getActionManager().setAction(new CollectTearAction(togWall, object));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.WEEPING_WALL};
    }
}
