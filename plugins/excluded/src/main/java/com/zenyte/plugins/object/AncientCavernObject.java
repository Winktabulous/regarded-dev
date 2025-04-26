package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 5 jun. 2018 | 20:09:26
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class AncientCavernObject implements ObjectAction {

    private static final Location WATERFIENDS_DOWN_STAIRS = new Location(1772, 5366, 0);

    private static final Location WATERFIENDS_UP_STAIRS = new Location(1768, 5366, 1);

    private static final Location MITHRIL_DRAGONS_DOWN_STAIRS = new Location(1778, 5346, 0);

    private static final Location MITHRIL_DRAGONS_UP_STAIRS = new Location(1778, 5343, 1);

    private static final Location FORGE_UP_STAIRS = new Location(1744, 5321, 1);

    private static final Location FORGE_DOWN_STAIRS = new Location(1745, 5325, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == ObjectId.STAIRS_25336) {
            player.useStairs(828, WATERFIENDS_UP_STAIRS, 1, 2);
        } else if (object.getId() == ObjectId.STAIRS_25338) {
            player.useStairs(827, WATERFIENDS_DOWN_STAIRS, 1, 2);
        } else if (object.getId() == ObjectId.STAIRS_25339) {
            player.useStairs(828, MITHRIL_DRAGONS_UP_STAIRS, 1, 2);
        } else if (object.getId() == ObjectId.STAIRS_25340) {
            player.useStairs(827, MITHRIL_DRAGONS_DOWN_STAIRS, 1, 2);
        } else if (object.getId() == ObjectId.ROUGH_HEWN_STEPS) {
            player.useStairs(827, FORGE_DOWN_STAIRS, 1, 2);
        } else if (object.getId() == ObjectId.ROUGH_HEWN_STEPS_32212) {
            player.useStairs(828, FORGE_UP_STAIRS, 1, 2);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRS_25336, ObjectId.STAIRS_25338, ObjectId.STAIRS_25339, ObjectId.STAIRS_25340, ObjectId.ROUGH_HEWN_STEPS, ObjectId.ROUGH_HEWN_STEPS_32212 };
    }
}
