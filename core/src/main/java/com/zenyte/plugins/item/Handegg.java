package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 15/05/2019 23:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Handegg extends ItemPlugin {
    @Override
    public void handle() {
        bind("Hold", (player, item, slotId) -> player.getEquipment().wear(slotId));
    }

    @Override
    public int[] getItems() {
        return new int[] {
            22355, 22358, 22361
        };
    }
}
