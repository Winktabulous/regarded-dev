package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;

/**
 * @author Kris | 19/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CrierBell extends ItemPlugin {
    @Override
    public void handle() {
        bind("Ring", (player, item, slotId) -> player.setAnimation(new Animation(7268)));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.CRIER_BELL
        };
    }
}
