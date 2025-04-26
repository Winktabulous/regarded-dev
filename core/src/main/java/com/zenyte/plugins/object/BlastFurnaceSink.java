package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.FillContainer;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnaceSink implements ObjectAction {

    private static final Item BUCKET = new Item(1925, 1);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getInventory().containsItem(1925, 1)) {
            player.getActionManager().setAction(new FillContainer(object, BUCKET));
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SINK_9143 };
    }
}
