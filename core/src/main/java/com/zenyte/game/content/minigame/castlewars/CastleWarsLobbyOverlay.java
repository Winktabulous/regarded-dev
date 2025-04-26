package com.zenyte.game.content.minigame.castlewars;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLobbyOverlay extends Interface {

    public static final int TIMER_VARP = 380;

    @Override
    protected void attach() {}

    @Override
    protected void build() {}

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.CASTLE_WARS_LOBBY_OVERLAY;
    }
}
