package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsTeamLadder implements ObjectAction {
    private static final int SARADOMIN_LADDER = 6280;
    private static final int SARADOMIN_TRAPDOOR = 4471;
    private static final int ZAMORAK_LADDER = 6281;
    private static final int ZAMORAK_TRAPDOOR = 4472;
    private static final Location ZAMORAK_TOP = new Location(2370, 3132, 2);
    private static final Location ZAMORAK_BOTTOM = new Location(2370, 3132, 1);
    private static final Location SARADOMIN_TOP = new Location(2429, 3075, 2);
    private static final Location SARADOMIN_BOTTOM = new Location(2429, 3075, 1);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final CastleWarsTeam team = CastleWars.getTeam(player);
        final boolean trapdoor = object.getId() == ZAMORAK_TRAPDOOR || object.getId() == SARADOMIN_TRAPDOOR;
        if (!trapdoor && ((team.equals(CastleWarsTeam.SARADOMIN) && object.getId() != SARADOMIN_LADDER) || (team.equals(CastleWarsTeam.ZAMORAK) && object.getId() != ZAMORAK_LADDER))) {
            return;
        }
        if (!trapdoor) {
            player.setAnimation(Animation.LADDER_UP);
            WorldTasksManager.schedule(() -> {
                player.setLocation(object.getId() == SARADOMIN_LADDER ? SARADOMIN_TOP : ZAMORAK_TOP);
            }, 0);
        } else {
            if (team.equals(CastleWarsTeam.SARADOMIN) && object.getId() != SARADOMIN_TRAPDOOR) player.setLocation(object.getId() == SARADOMIN_TRAPDOOR ? SARADOMIN_BOTTOM : ZAMORAK_BOTTOM);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {SARADOMIN_LADDER, SARADOMIN_TRAPDOOR, ZAMORAK_LADDER, ZAMORAK_TRAPDOOR};
    }
}
