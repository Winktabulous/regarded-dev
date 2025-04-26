package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 4 mei 2018 | 00:01:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class StrongholdSlayerCaveEntranceObject implements ObjectAction {

    private static final Location INSIDE_LOCATION = new Location(2429, 9825, 0);

    private static final Location OUTSIDE_LOCATION = new Location(2430, 3424, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Location location = object.getId() == 26709 ? INSIDE_LOCATION : OUTSIDE_LOCATION;
        player.setLocation(location);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_26709, ObjectId.TUNNEL_27257, ObjectId.TUNNEL_27258 };
    }
}
