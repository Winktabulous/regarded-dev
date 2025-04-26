package com.zenyte.game.content.trouver;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

public class TrouverItemDeathPlugin extends ItemPlugin {

    @Override
    public void handle() {

    }

    @Override
    public int[] getItems() {
        return TrouverData.PROTECTED_ITEMS;
    }


}
