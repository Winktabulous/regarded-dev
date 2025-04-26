package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.Gate;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 12. dets 2017 : 17:52.57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GateObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        Gate.handleGate(object);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_47, ObjectId.GATE_48, ObjectId.GATE_166, ObjectId.GATE_167, ObjectId.GATE_883, ObjectId.GATE_1558, ObjectId.GATE_1559, ObjectId.GATE_1560, ObjectId.GATE_1561, ObjectId.GATE_1562, ObjectId.GATE_1563, ObjectId.GATE_1564, ObjectId.GATE_1567, ObjectId.GATE_1730, ObjectId.GATE_1731, ObjectId.GATE_2050, ObjectId.GATE_2051, ObjectId.GATE_2438, ObjectId.GATE_2439, ObjectId.GATE_3725, ObjectId.GATE_3726, ObjectId.GATE_3727, ObjectId.GATE_3728, ObjectId.GATE_4311, ObjectId.GATE_4312, ObjectId.GATE_8810, ObjectId.GATE_8811, ObjectId.GATE_8812, ObjectId.GATE_8813, ObjectId.GATE_9470, ObjectId.GATE_9708, ObjectId.GATE_12816, ObjectId.GATE_12817, ObjectId.GATE_12818, ObjectId.GATE_12986, ObjectId.GATE_12987, ObjectId.GATE_12988, ObjectId.GATE_12989, ObjectId.GATE_15510, ObjectId.GATE_15511, ObjectId.GATE_15512, ObjectId.GATE_15513, ObjectId.GATE_15514, ObjectId.GATE_15515, ObjectId.GATE_15516, ObjectId.GATE_15517, ObjectId.GATE_23917, ObjectId.GATE_23918, ObjectId.GATE_23919, ObjectId.GATE_24560, ObjectId.GATE_24561 };
    }
}
