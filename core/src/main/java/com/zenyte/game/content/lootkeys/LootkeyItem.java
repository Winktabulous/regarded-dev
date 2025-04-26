package com.zenyte.game.content.lootkeys;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;

public class LootkeyItem extends ItemPlugin {

    @Override
    public void handle() {
        bind("Check", ((player, item, container, slotId) -> {

            var itemValue = item.getNumericAttribute(LootkeyConstants.LOOT_KEY_ITEM_LOOT_VALUE_ATTR).longValue();
            player.sendMessage("Your loot key contains items that are worth approximately "
                    + Utils.formatNumberWithCommas(itemValue) + "gp. ");

        }));
    }

    @Override
    public int[] getItems() {
        return new int[] { LootkeyConstants.LOOT_KEY_ITEM_ID };
    }
}
