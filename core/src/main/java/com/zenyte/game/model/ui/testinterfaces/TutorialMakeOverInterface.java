package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 1-4-2019 | 21:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TutorialMakeOverInterface extends Interface {

    @Override
    protected void attach() {
        put(97, "Title");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Title"), "Welcome to " + GameConstants.SERVER_NAME + "<br>Use the buttons below to design your player");
    }

    @Override
    protected void build() {

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.TUTORIAL_MAKE_OVER;
    }
}
