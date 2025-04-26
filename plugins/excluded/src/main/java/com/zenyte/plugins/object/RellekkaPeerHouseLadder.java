package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24/04/2019 22:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RellekkaPeerHouseLadder implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.LADDER_4164) {
            player.sendMessage("You can't go up this way!");
            return;
        }
        player.lock(2);
        player.setAnimation(new Animation(828));
        WorldTasksManager.schedule(() -> player.setLocation(new Location(player.getX(), player.getY(), 2)));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_4163, ObjectId.LADDER_4164 };
    }
}
