package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 12/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GnomeStrongholdOpening implements ObjectAction {

    private static final Location mainlandInside = new Location(2435, 3520, 0);

    private static final Location mainland = new Location(2435, 3519, 0);

    private static final Location offland = new Location(1987, 5567, 0);

    private static final Location offlandInside = new Location(1987, 5568, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock(2);
        if (player.getLocation().matches(mainland) || player.getLocation().matches(mainlandInside)) {
            final boolean outside = player.getLocation().matches(mainland);
            player.setLocation(outside ? offland : offlandInside);
            WorldTasksManager.schedule(() -> {
                final Location tile = outside ? offlandInside : offland;
                player.addWalkSteps(tile.getX(), tile.getY(), 1, false);
            });
        } else {
            final boolean outside = player.getLocation().matches(offland);
            player.setLocation(outside ? mainland : mainlandInside);
            WorldTasksManager.schedule(() -> {
                final Location tile = outside ? mainlandInside : mainland;
                player.addWalkSteps(tile.getX(), tile.getY(), 1, false);
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 28807, ObjectId.OPENING_28656 };
    }
}
