package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 */
public final class TarBarrel implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getInventory().addItem(1939, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TAR_BARREL_16860 };
    }
}
