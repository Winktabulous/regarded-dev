package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.object.MudPit;

/**
 * @author Chris
 * @since July 20 2020
 */
public class MushroomOnMudPit implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        MudPit.attemptFill(player);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.MUSHROOM };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 31426 };
    }
}
