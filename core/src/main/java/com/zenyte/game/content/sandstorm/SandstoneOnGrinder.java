package com.zenyte.game.content.sandstorm;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Chris
 * @since August 20 2020
 */
public class SandstoneOnGrinder implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        Grinder.deposit(player);
    }

    @Override
    public Object[] getItems() {
        return Sandstone.SANDSTONE_IDS.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.GRINDER};
    }
}
