package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 16-3-2019 | 17:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DecantingDialogueInterface extends Interface {

    @Override
    protected void attach() {
        put(3, "1-dose");
        put(4, "2-dose");
        put(5, "3-dose");
        put(6, "4-dose");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("1-dose", player -> player.getDialogueManager().onClick(1, -1));
        bind("2-dose", player -> player.getDialogueManager().onClick(2, -1));
        bind("3-dose", player -> player.getDialogueManager().onClick(3, -1));
        bind("4-dose", player -> player.getDialogueManager().onClick(4, -1));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.DECANTING;
    }
}
