package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 23/06/2019 15:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EctofuntusStaircase implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Climb-down")) {
            switch(player.getPlane()) {
                case 3:
                    player.setLocation(new Location(3688, 9888, 2));
                    return;
                case 2:
                    player.setLocation(new Location(3675, 9888, 1));
                    return;
                case 1:
                    player.setLocation(new Location(3683, 9888, 0));
                    return;
            }
        } else {
            switch(player.getPlane()) {
                case 0:
                    player.setLocation(new Location(3687, 9888, 1));
                    return;
                case 1:
                    player.setLocation(new Location(3671, 9888, 2));
                    return;
                case 2:
                    player.setLocation(new Location(3692, 9888, 3));
                    return;
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRS_16109, ObjectId.STAIRS_16108 };
    }
}
