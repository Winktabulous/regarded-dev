package com.zenyte.game.content.treasuretrails.coordinateutils;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 03/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SextantItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Look through", (player, item, slotId) -> GameInterface.SEXTANT.open(player));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.SEXTANT
        };
    }
}
