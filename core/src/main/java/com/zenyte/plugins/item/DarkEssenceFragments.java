package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 24/04/2019 23:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DarkEssenceFragments extends ItemPlugin {
    @Override
    public void handle() {
        bind("Count", (player, item, container, slotId) -> player.sendMessage("You currently hold " + item.getCharges() + " dark essence fragments."));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                7938
        };
    }
}
