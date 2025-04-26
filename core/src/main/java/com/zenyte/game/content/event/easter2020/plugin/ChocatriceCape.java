package com.zenyte.game.content.event.easter2020.plugin;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.plugins.SkipPluginScan;

/**
 * @author Kris | 11/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class ChocatriceCape extends ItemPlugin {

    private static final Animation animation = new Animation(15191);
    private static final Graphics graphics = new Graphics(2509);

    @Override
    public void handle() {
        bind("Operate", (player, item, container, slotId) -> {
            player.lock(6);
            player.setGraphics(graphics);
            player.setInvalidAnimation(animation);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                EasterConstants.EasterItem.CHOCATRICE_CAPE.getItemId()
        };
    }
}
