package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/04/2019 02:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LighthouseStrangeWall implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Study")) {
            player.lock(2);
            player.sendMessage("You study the wall..");
            WorldTasksManager.schedule(() -> player.sendMessage(".. and realize there should be a door nearby."), 2);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STRANGE_WALL, ObjectId.STRANGE_WALL_4544 };
    }
}
