package com.zenyte.game.content.minigame.puropuro;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Corey
 * @since 29/01/2020
 */
public class ButterflyNetItem extends ItemPlugin {
    
    @Override
    public void handle() {
        bind("Check totals", (player, item, slotId) -> GameInterface.IMPLING_TRACKER.open(player));
    }
    
    @Override
    public int[] getItems() {
        return new int[]{ItemId.MAGIC_BUTTERFLY_NET, ItemId.BUTTERFLY_NET};
    }
    
}
