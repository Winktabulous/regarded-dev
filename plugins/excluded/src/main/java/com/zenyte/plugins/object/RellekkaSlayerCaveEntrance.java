package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 27 mei 2018 | 00:15:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RellekkaSlayerCaveEntrance implements ObjectAction {

    private static final Location INSIDE_LOCATION = new Location(2808, 10002, 0);

    private static final Location OUTSIDE_LOCATION = new Location(2796, 3615, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setLocation(object.getId() == 2123 ? INSIDE_LOCATION : OUTSIDE_LOCATION);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_ENTRANCE_2123, ObjectId.TUNNEL };
    }
}
