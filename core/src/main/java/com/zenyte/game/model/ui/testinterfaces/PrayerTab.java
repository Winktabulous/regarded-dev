package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16/04/2019 16:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PrayerTab extends Interface {
    @Override
    protected void attach() {

    }

    @Override
    protected DefaultClickHandler getDefaultHandler() {
        return (player, componentId, slotId, itemId, optionId) -> {
            if (componentId >= 5 && componentId <= 33) {
                player.getPrayerManager().togglePrayer(componentId - 5, false);
            }
        };
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PRAYER_TAB_INTERFACE;
    }
}
