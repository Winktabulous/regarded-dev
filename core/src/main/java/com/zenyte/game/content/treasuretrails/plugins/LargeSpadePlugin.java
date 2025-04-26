package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 05/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LargeSpadePlugin extends ItemPlugin {
    @Override
    public void handle() {
        bind("Dig", (player, item, container, slotId) -> player.sendMessage("Due to its sheer size, you just cannot angle it right to dig with it."));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.LARGE_SPADE
        };
    }
}
