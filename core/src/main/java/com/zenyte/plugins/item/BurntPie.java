package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Christopher
 * @since 1/23/2020
 */
public class BurntPie extends ItemPlugin {

    @Override
    public void handle() {
        bind("Empty Dish", (player, item, slotId) -> {
            player.getInventory().set(slotId, new Item(ItemId.PIE_DISH, 1));
            player.sendMessage("You remove the burnt pie from the pie dish.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { ItemId.BURNT_PIE };
    }
}