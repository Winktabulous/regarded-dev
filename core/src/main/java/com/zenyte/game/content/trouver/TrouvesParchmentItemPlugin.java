package com.zenyte.game.content.trouver;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

public class TrouvesParchmentItemPlugin extends ItemPlugin {

    @Override
    public void handle() {
        bind("Read", (player, item, slot) -> {
            player.sendFilteredMessage("This is a powerful scroll. Perdu, the dwarf, can use it to help lock certain items to their owners, so that the items aren't destroyed on death in deep Wilderness.");
        });

    }

    @Override
    public int[] getItems() {
        return new int[] {TrouverConstants.TROUVER_PARCHMENT_ITEM_ID };
    }
}
