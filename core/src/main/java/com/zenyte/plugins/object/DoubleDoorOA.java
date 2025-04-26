package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.DoubleDoor;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10. veebr 2018 : 22:27.12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DoubleDoorOA implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Open") || option.equalsIgnoreCase("Close")) {
            DoubleDoor.handleDoubleDoor(player, object);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LARGE_DOOR_12349, ObjectId.LARGE_DOOR_12350, ObjectId.GATE_1727, ObjectId.GATE_1728, ObjectId.GATE_1568, ObjectId.GATE_1569, ObjectId.LARGE_DOOR_1521, ObjectId.LARGE_DOOR_1524, ObjectId.PRISON_DOOR_2143, ObjectId.PRISON_DOOR_2144, ObjectId.LARGE_DOOR_73, ObjectId.LARGE_DOOR_74, ObjectId.LARGE_DOOR_1513, ObjectId.LARGE_DOOR_1511, ObjectId.METAL_GATE_2260, ObjectId.METAL_GATE, ObjectId.LONGHALL_DOOR_11621, ObjectId.LONGHALL_DOOR, ObjectId.DOOR_2624, ObjectId.DOOR_2625, ObjectId.GATE_26081, ObjectId.GATE_26082, ObjectId.LARGE_DOOR_25638, ObjectId.LARGE_DOOR_25640, ObjectId.DOOR_25825, ObjectId.DOOR_25827, ObjectId.DOOR_14751, ObjectId.DOOR_14752, ObjectId.LARGE_DOOR_25813, ObjectId.LARGE_DOOR_25814, ObjectId.LARGE_DOOR_25748, ObjectId.LARGE_DOOR_25750, ObjectId.GATE_26130, ObjectId.GATE_26131, ObjectId.GATE_52, ObjectId.GATE_53, ObjectId.DOOR_2546, ObjectId.DOOR_2548, ObjectId.LARGE_DOOR_17091, ObjectId.LARGE_DOOR_17093, ObjectId.LARGE_DOOR_134, ObjectId.LARGE_DOOR_135, ObjectId.LARGE_DOOR_30387, ObjectId.LARGE_DOOR_30388, ObjectId.DOOR_1551, ObjectId.DOOR_1549, ObjectId.GATE_2115, ObjectId.GATE_2116, ObjectId.DOOR_21505, ObjectId.DOOR_21507, ObjectId.GATE_21405, ObjectId.GATE_21403, ObjectId.DOOR_10262, ObjectId.DOOR_10263, ObjectId.DOOR_10264, ObjectId.DOOR_10265, 3489, 3490, ObjectId.DOOR_24565, ObjectId.DOOR_24567, ObjectId.GLASS_DOOR_27485, ObjectId.GLASS_DOOR_27486, ObjectId.DOOR_2108, ObjectId.DOOR_2111, ObjectId.HARDWOOD_GROVE_DOORS, ObjectId.HARDWOOD_GROVE_DOORS_9039, ObjectId.MAGIC_DOOR_12045, ObjectId.MAGIC_DOOR_12047, ObjectId.GATE_11766, ObjectId.GATE_11767, ObjectId.GATE_3506, ObjectId.GATE_3507, ObjectId.DOOR_35009, ObjectId.DOOR_35010, ObjectId.DOOR_35011, ObjectId.DOOR_35012, ObjectId.DOOR_28456, ObjectId.DOOR_28457, ObjectId.DOOR_28458, ObjectId.DOOR_28459, ObjectId.DOOR_28460, ObjectId.DOOR_28461, ObjectId.DOOR_28462, ObjectId.DOOR_28463, ObjectId.DOOR_28464, ObjectId.DOOR_28465, ObjectId.DOOR_28466, ObjectId.DOOR_28467, ObjectId.DOOR_28468, ObjectId.DOOR_28469, ObjectId.DOOR_28470, ObjectId.DOOR_28471, ObjectId.DOOR_33570, ObjectId.DOOR_33572, ObjectId.LARGE_DOOR_12446, ObjectId.LARGE_DOOR_12448, ObjectId.DOOR_5183, ObjectId.DOOR_5186, ObjectId.DOOR_5187, ObjectId.DOOR_5188 };
    }
}
