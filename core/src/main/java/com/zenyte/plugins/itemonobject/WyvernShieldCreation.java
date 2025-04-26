package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.object.StrangeMachine;

public class WyvernShieldCreation implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        StrangeMachine.start(player, object);
    }

    @Override
    public Object[] getItems() {
        return StrangeMachine.requiredItems.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { StrangeMachine.STRANGE_MACHINE_ID };
    }
}
