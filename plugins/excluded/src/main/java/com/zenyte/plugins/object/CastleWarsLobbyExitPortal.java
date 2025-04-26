package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.content.minigame.castlewars.CastleWarsLobby.MAIN_LOBBY_SPAWN;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLobbyExitPortal implements ObjectAction {

    private static final int ZAMORAK_PORTAL = 4390;
    private static final int SARADOMIN_PORTAL = 4389;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if(!player.inArea("Castle Wars Lobby")) {
            return;
        }

        player.setLocation(MAIN_LOBBY_SPAWN);
        return;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { SARADOMIN_PORTAL, ZAMORAK_PORTAL };
    }
}
