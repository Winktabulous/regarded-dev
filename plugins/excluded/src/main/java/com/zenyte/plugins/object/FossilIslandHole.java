package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 20:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FossilIslandHole implements ObjectAction {

    private static final Animation CRAWL = new Animation(844);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.lock(2);
        player.setAnimation(CRAWL);
        WorldTasksManager.schedule(() -> {
            if (object.getId() == ObjectId.HOLE_31482) {
                player.setLocation(new Location(3715, 3815, 0));
            } else {
                player.setLocation(new Location(3713, 3830, 0));
            }
        }, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.HOLE_31481, ObjectId.HOLE_31482 };
    }
}
