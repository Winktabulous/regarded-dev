package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 06/05/2019 22:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ApeAtollBambooGate implements ObjectAction {

    private static final WorldObject left = new WorldObject(4787, 10, 0, 2719, 2766, 0);

    private static final WorldObject right = new WorldObject(4788, 10, 2, 2721, 2766, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        World.removeObject(left);
        World.removeObject(right);
        final WorldObject first = new WorldObject(left);
        first.setId(first.getId() + 2);
        World.spawnObject(first);
        final WorldObject second = new WorldObject(right);
        second.setId(second.getId() + 2);
        second.moveLocation(1, 0, 0);
        World.spawnObject(second);
        player.lock(2);
        player.setRunSilent(2);
        if (player.getY() <= 2765) {
            player.addWalkSteps(player.getX(), player.getY() + 2, -1, false);
        } else {
            player.addWalkSteps(player.getX(), player.getY() - 2, -1, false);
        }
        WorldTasksManager.schedule(() -> {
            World.removeObject(first);
            World.removeObject(second);
            World.spawnObject(left);
            World.spawnObject(right);
        }, 2);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BAMBOO_GATE, ObjectId.BAMBOO_GATE_4788 };
    }
}
