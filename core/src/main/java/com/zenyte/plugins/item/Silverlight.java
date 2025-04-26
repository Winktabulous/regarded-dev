package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Tommeh | 13-3-2019 | 21:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Silverlight extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, slotId) -> {
            final int kills = player.getNumericAttribute("demon_kills").intValue();
            final int remaining = 100 - kills;
            player.sendMessage(kills == 100 ? "You have slain 100 demons." : "You've slain " + kills + " demons so far, " + remaining + " more to upgrade your weapon into a Darklight!");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {2402};
    }
}
