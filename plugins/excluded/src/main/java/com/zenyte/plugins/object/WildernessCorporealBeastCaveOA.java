package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10. veebr 2018 : 3:07.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WildernessCorporealBeastCaveOA implements ObjectAction {

    private static final Location TILE = new Location(2964, 4382, 2);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getVariables().getTime(TickVariable.TELEBLOCK) > 0) {
            player.sendMessage("You cannot enter the cave while teleblocked.");
            return;
        }
        if (player.isUnderCombat()) {
            player.sendMessage("You cannot enter the cave while in combat.");
            return;
        }
        player.lock(2);
        player.setLocation(TILE);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE };
    }
}
