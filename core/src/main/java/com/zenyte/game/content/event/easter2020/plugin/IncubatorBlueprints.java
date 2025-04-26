package com.zenyte.game.content.event.easter2020.plugin;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.plugins.SkipPluginScan;

/**
 * @author Kris | 09/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class IncubatorBlueprints extends ItemPlugin {
    @Override
    public void handle() {
        bind("Read", (player, item, slotId) -> player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 712));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                EasterConstants.EasterItem.INCUBATOR_BLUEPRINTS.getItemId()
        };
    }
}
