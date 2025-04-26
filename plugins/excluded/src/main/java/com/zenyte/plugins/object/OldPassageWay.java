package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 19/04/2019 17:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OldPassageWay implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Leave")) {
            if (object.matches(new Location(3013, 9951, 0))) {
                player.setLocation(new Location(3037, 3382, 0));
            } else {
                player.setLocation(new Location(3054, 3382, 0));
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.OLD_PASSAGEWAY_31892 };
    }
}
