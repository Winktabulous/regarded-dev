package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;

/**
 * @author Tommeh | 28-10-2018 | 15:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class IgnoreListInterface extends Interface {

    @Override
    protected void attach() {
        put(1, "View Friends List");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("View Friends List", player -> player.getSettings().toggleSetting(Setting.FRIEND_LIST_TOGGLE));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.IGNORE_LIST_TAB;
    }
}
