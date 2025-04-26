package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 26/06/2019 14:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SwampToad extends ItemPlugin {
    @Override
    public void handle() {
        bind("Remove-legs", (player, item, container, slotId) -> {
            player.getInventory().deleteItem(item);
            player.getInventory().addOrDrop(new Item(2152));
            player.sendMessage("You pull the legs off the toad. Poor toad. At least they'll grow back.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                2150
        };
    }
}
