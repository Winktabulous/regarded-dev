package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 04/07/2019 23:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HandFan extends ItemPlugin {
    @Override
    public void handle() {
        bind("Chill", (player, item, container, slotId) -> player.getEquipment().wear(slotId));
    }

    @Override
    public int[] getItems() {
        return new int[] {
               21354
        };
    }
}
