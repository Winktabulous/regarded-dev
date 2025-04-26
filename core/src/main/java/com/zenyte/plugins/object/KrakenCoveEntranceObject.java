package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 21 mei 2018 | 16:27:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KrakenCoveEntranceObject implements ObjectAction {

    private static final Location INSIDE_LOCATION = new Location(2276, 9988, 0);

    private static final Location OUTSIDE_LOCATION = new Location(2278, 3610, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setLocation(object.getId() == 30177 ? INSIDE_LOCATION : OUTSIDE_LOCATION);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_ENTRANCE_30177, ObjectId.CAVE_EXIT_30178 };
    }
}
