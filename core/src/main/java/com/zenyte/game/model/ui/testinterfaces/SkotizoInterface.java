package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 06/03/2020 | 20:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class SkotizoInterface extends Interface {

    @Override
    protected void attach() {

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
        return GameInterface.SKOTIZO_OVERLAY;
    }
}
