package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;

/**
 * @author Chris
 * @since August 18 2020
 */
public class RubberChicken extends ItemPlugin {
    private static final Animation danceAnim = new Animation(1835);

    @Override
    public void handle() {
        bind("Dance", (player, item, slotId) -> player.setAnimation(danceAnim));
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.RUBBER_CHICKEN};
    }
}
