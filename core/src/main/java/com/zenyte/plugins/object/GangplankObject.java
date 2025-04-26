package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 7 jul. 2018 | 22:41:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GangplankObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Cross")) {
            Location location = null;
            final int plane = player.getPlane() == 0 ? 1 : 0;
            if (object.getRotation() == 0 || object.getRotation() == 2) {
                if (player.getX() > object.getX()) {
                    location = new Location(player.getX() - 3, player.getY(), plane);
                } else {
                    location = new Location(player.getX() + 3, player.getY(), plane);
                }
            } else if (object.getRotation() == 1 || object.getRotation() == 3) {
                if (player.getY() > object.getY()) {
                    location = new Location(player.getX(), player.getY() - 3, plane);
                } else {
                    location = new Location(player.getX(), player.getY() + 3, plane);
                }
            }
            player.setLocation(location);
            player.sendMessage("You cross the gangplank.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GANGPLANK_2081, ObjectId.GANGPLANK_2082, ObjectId.GANGPLANK_2083, ObjectId.GANGPLANK_2084, ObjectId.GANGPLANK_2085, ObjectId.GANGPLANK_2086, ObjectId.GANGPLANK_2087, ObjectId.GANGPLANK_2088, ObjectId.GANGPLANK_2412, ObjectId.GANGPLANK_2413, ObjectId.GANGPLANK_2414, ObjectId.GANGPLANK_2415, ObjectId.GANGPLANK_14304, ObjectId.GANGPLANK_14305, ObjectId.GANGPLANK_14306, ObjectId.GANGPLANK_14307, ObjectId.GANGPLANK_17392, ObjectId.GANGPLANK_17393, ObjectId.GANGPLANK_17394, ObjectId.GANGPLANK_17395, ObjectId.GANGPLANK_17396, ObjectId.GANGPLANK_17397, ObjectId.GANGPLANK_17398, ObjectId.GANGPLANK_17399, ObjectId.GANGPLANK_17400, ObjectId.GANGPLANK_17401, ObjectId.GANGPLANK_17402, ObjectId.GANGPLANK_17403, ObjectId.GANGPLANK_17404, ObjectId.GANGPLANK_17405, ObjectId.GANGPLANK_17406, ObjectId.GANGPLANK_17407, ObjectId.GANGPLANK_17408, ObjectId.GANGPLANK_17409, ObjectId.GANGPLANK_27777, ObjectId.GANGPLANK_27778, ObjectId.ALCHEMICAL_HYDRA, ObjectId.SCORE_BOARD, ObjectId.GANGPLANK_29723, ObjectId.GANGPLANK_29724, ObjectId.GANGPLANK_31756, ObjectId.GANGPLANK_34672, ObjectId.GANGPLANK_34949 };
    }
}
