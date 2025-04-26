package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.content.minigame.castlewars.CastleWars.SARADOMIN_TEAM;
import static com.zenyte.game.content.minigame.castlewars.CastleWars.ZAMORAK_TEAM;
import static com.zenyte.game.content.minigame.castlewars.CastleWarsLobby.*;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsExitPortal implements ObjectAction {
    private static final int SARADOMIN_PORTAL = 4406;
    private static final int ZAMORAK_PORTAL = 4407;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final CastleWarsTeam team = CastleWars.getTeam(player);
        final boolean saradomin = team.equals(CastleWarsTeam.SARADOMIN);
        if (saradomin && object.getId() != SARADOMIN_PORTAL || !saradomin && object.getId() != ZAMORAK_PORTAL) {
            return;
        }
        CastleWars.removeCwarsItems(player, false);
        player.setLocation(saradomin ? SARADOMIN_LOBBY_SPAWN : ZAMORAK_LOBBY_SPAWN);
        (saradomin ? SARADOMIN_LOBBY : ZAMORAK_LOBBY).add(player);
        (saradomin ? SARADOMIN_TEAM : ZAMORAK_TEAM).remove(player);
        if (SARADOMIN_TEAM.size() == 0 && ZAMORAK_TEAM.size() == 0) {
            CastleWars.finish();
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {SARADOMIN_PORTAL, ZAMORAK_PORTAL};
    }
}
