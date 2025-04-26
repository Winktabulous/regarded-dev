package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 21/04/2019 12:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class QuickPrayerInterface extends Interface {
    @Override
    protected void attach() {
        put(4, "Select prayer");
        put(5, "Confirm");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Select prayer", (player, slotId, itemId, option) -> player.getPrayerManager().setQuickPrayer(slotId));
        bind("Confirm", GameInterface.PRAYER_TAB_INTERFACE::open);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.QUICK_PRAYERS;
    }
}
