package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27. mai 2018 : 18:35:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class WarriorsGuildLadderOA implements ObjectAction {

    private static final Location BASEMENT = new Location(2907, 9968, 0);

    private static final Location GROUND = new Location(2834, 3542, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Climb-up")) {
            player.useStairs(828, GROUND, 1, 1);
        } else {
            player.useStairs(827, BASEMENT, 1, 1);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_10042, ObjectId.LADDER_9742 };
    }
}
