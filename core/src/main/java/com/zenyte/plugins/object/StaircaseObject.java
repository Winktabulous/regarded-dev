package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.ObjectDefinitions;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 19. veebr 2018 : 17:39.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class StaircaseObject implements ObjectAction {

    private static final int[] LARGE_STAIRCASES = new int[] { 25604, 11499, 2118, 2120, 16647 };

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final ObjectDefinitions defs = object.getDefinitions();
        final boolean up = option.equals("Climb-up") || option.equals("Walk-up");
        final int plane = player.getPlane() + (up ? 1 : -1);
        if (up && plane > 3 || !up && plane < 0) {
            return;
        }
        if (defs.getSizeY() >= 3) {
            final int offset = defs.getSizeY() + 1;
            switch(object.getRotation()) {
                case 0:
                    player.setLocation(new Location(player.getX(), player.getY() + offset, plane));
                    return;
                case 1:
                    player.setLocation(new Location(player.getX() + offset, player.getY(), plane));
                    return;
                case 2:
                    player.setLocation(new Location(player.getX(), player.getY() - offset, plane));
                    return;
                case 3:
                    player.setLocation(new Location(player.getX() - offset, player.getY(), plane));
                    return;
            }
        } else if (defs.getSizeY() == 2) {
            final int offset = 4 + (ArrayUtils.contains(LARGE_STAIRCASES, object.getId()) ? 1 : 0);
            switch(object.getRotation()) {
                case 0:
                    player.setLocation(new Location(player.getX(), player.getY() - offset, plane));
                    return;
                case 1:
                    player.setLocation(new Location(player.getX() - offset, player.getY(), plane));
                    return;
                case 2:
                    player.setLocation(new Location(player.getX(), player.getY() + offset, plane));
                    return;
                case 3:
                    player.setLocation(new Location(player.getX() + offset, player.getY(), plane));
                    return;
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRCASE, ObjectId.STAIRCASE_2119, ObjectId.STAIRCASE_2121, ObjectId.STAIRCASE_5206, ObjectId.STAIRCASE_6648, ObjectId.STAIRCASE_10525, ObjectId.STAIRCASE_11498, ObjectId.STAIRCASE_15645, ObjectId.STAIRCASE_15646, ObjectId.STAIRCASE_15647, ObjectId.STAIRCASE_15649, ObjectId.STAIRCASE_15653, ObjectId.STAIRCASE_16646, ObjectId.STAIRCASE_16661, ObjectId.STAIRCASE_18991, ObjectId.STAIRCASE_24672, ObjectId.STAIRCASE_25786, ObjectId.STAIRCASE_26106, ObjectId.STAIRCASE_30139, ObjectId.STAIRCASE_2118, ObjectId.STAIRCASE_2120, ObjectId.STAIRCASE_2122, ObjectId.STAIRCASE_5207, ObjectId.STAIRCASE_6649, ObjectId.STAIRCASE_10526, ObjectId.ORNAMENTAL_GLOBE, ObjectId.LUNAR_GLOBE, ObjectId.CELESTIAL_GLOBE, ObjectId.STAIRCASE_15648, ObjectId.STAIRCASE_15650, ObjectId.STAIRCASE_15652, ObjectId.STAIRCASE_15654, ObjectId.STAIRCASE_16647, ObjectId.STAIRCASE_16662, ObjectId.STAIRCASE_16663, ObjectId.STAIRCASE_18992, ObjectId.STAIRCASE_24673, ObjectId.STAIRCASE_25787, ObjectId.STAIRS_4756, ObjectId.STAIRS_4755 };
    }
}
