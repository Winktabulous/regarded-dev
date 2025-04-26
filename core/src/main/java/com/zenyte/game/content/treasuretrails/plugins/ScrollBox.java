package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 23/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScrollBox extends ItemPlugin {
    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> {
            final int index = ArrayUtils.indexOf(ClueItem.getBoxesArray(), item.getId());
            if (index == -1) {
                return;
            }
            final int clue = ClueItem.getCluesArray()[index];
            if (player.containsItem(clue)) {
                player.sendMessage("You can only have one " + ItemDefinitions.getOrThrow(clue).getName().toLowerCase() + " with you at a time.");
                return;
            }
            if (!(item.getAmount() == 1 || player.getInventory().hasFreeSlots())) {
                player.sendMessage("You need some free inventory space to open the box.");
                return;
            }
            player.getInventory().ifDeleteItem(new Item(item.getId(), 1), () -> player.getInventory().addOrDrop(new Item(clue)));
        });
    }

    @Override
    public int[] getItems() {
        return ClueItem.getBoxesArray();
    }
}
