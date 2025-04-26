package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Kris | 29/11/2018 10:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BugReportInterface extends Interface {
    @Override
    protected void attach() {

    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {

    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        player.getPacketDispatcher().sendClientScript(2158);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BUG_REPORT_FORM;
    }
}
