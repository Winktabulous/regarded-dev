package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 06/05/2019 13:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CompostPlugin extends ItemPlugin {
    @Override
    public void handle() {
        bind("Empty", (player, item, container, slotId) -> {
           player.getInventory().deleteItem(slotId, item);
           player.getInventory().addOrDrop(new Item(1925));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                6032, 6034, 21483
        };
    }
}
