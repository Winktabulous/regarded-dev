package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;

/**
 * @author Kris | 16/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CandyCane extends ItemPlugin {
    private static final Animation animation = new Animation(15085);
    @Override
    public void handle() {
        bind("Spin", (player, item, slotId) -> player.setInvalidAnimation(animation));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                30105
        };
    }
}
