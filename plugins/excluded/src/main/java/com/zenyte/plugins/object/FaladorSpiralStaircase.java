package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 19/04/2019 17:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FaladorSpiralStaircase implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Climb-down")) {
            player.setLocation(object.getId() == 24248 ? new Location(3014, 9951, 0) : new Location(3065, 9951, 0));
        } else if (option.equalsIgnoreCase("Climb-up")) {
            player.setLocation(object.getId() == 24248 ? new Location(3039, 3383, 1) : new Location(3052, 3383, 1));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRCASE_24248, ObjectId.STAIRCASE_24249 };
    }
}
