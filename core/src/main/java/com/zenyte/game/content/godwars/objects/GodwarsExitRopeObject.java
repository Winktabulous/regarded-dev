package com.zenyte.game.content.godwars.objects;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 24-3-2019 | 13:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodwarsExitRopeObject implements ObjectAction {

    private static final Location OUTSIDE_LOCATION = new Location(2916, 3745, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
        player.useStairs(828, OUTSIDE_LOCATION, 1, 0, "You climb up the rope.");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROPE_26370 };
    }
}
