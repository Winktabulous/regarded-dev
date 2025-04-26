package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 18/06/2019 15:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GricollersCan extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> player.sendMessage("Your Gricoller's can is completely full and doesn't seem to be running out of water any time soon."
        ));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                13353
        };
    }
}
