package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 05/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HeavyCasket extends ItemPlugin {
    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> player.sendMessage("You try with all your might to open it, but you cannot."));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.HEAVY_CASKET
        };
    }
}
