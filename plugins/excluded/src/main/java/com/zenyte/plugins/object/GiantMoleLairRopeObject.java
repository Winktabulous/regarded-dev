package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 02/05/2019 | 21:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GiantMoleLairRopeObject implements ObjectAction {

    private static final Location DESTINATION = new Location(2987, 3315, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.useStairs(828, DESTINATION, 1, 2);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROPE_12230 };
    }
}
