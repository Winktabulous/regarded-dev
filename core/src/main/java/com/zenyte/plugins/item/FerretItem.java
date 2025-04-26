package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 23/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FerretItem extends ItemPlugin {

    @Override
    public void handle() {
        bind("Release", (player, item, container, slotId) -> {
            player.getInventory().deleteItem(slotId, item);
            player.sendFilteredMessage("You release the Ferret and it runs away.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{
                ItemId.FERRET
        };
    }

}
