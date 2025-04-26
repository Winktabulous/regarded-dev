package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.TemporaryDoubleDoor;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26. aug 2018 : 19:39:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class PiscatorisFishingColonyGate implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        TemporaryDoubleDoor.handleDoubleDoor(player, object);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.COLONY_GATE, ObjectId.COLONY_GATE_12725 };
    }
}
