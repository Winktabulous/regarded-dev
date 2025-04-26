package com.zenyte.plugins.object;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Chris
 * @since August 18 2020
 */
public class MageBankKnifeSack implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.carryingAny(ItemId.KNIFE)) {
            player.sendMessage("You find a knife inside the sack.");
            player.getInventory().addOrDrop(ItemId.KNIFE);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.SACK_14743};
    }
}
