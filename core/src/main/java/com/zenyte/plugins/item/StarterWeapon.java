package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Tommeh | 20/05/2019 | 18:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class StarterWeapon extends ItemPlugin {

    @Override
    public void handle() {
        bind("Check", (player, item, slotId) -> player.getChargesManager().checkCharges(item));
    }

    @Override
    public int[] getItems() {
        return new int[] { 22333, 22335 };
    }
}
