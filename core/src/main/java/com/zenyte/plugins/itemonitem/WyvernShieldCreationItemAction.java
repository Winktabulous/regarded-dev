package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.object.StrangeMachine;

public class WyvernShieldCreationItemAction implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.sendMessage("Perhaps some of the magical apparatus on Fossil Island can help join these two items.");
    }

    @Override
    public int[] getItems() {
        return StrangeMachine.requiredItems.toIntArray();
    }
}
