package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 06/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BirthdayCakeItem extends ItemPlugin {

    @Override
    public void handle() {
        bind("Hold", (player, item, slotId) -> player.getEquipment().wear(slotId));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.BIRTHDAY_CAKE
        };
    }

}