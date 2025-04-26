package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 19:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MythsGuildDungeonBarrier implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Pass")) {
            player.lock(2);
            player.setRunSilent(2);
            if (player.getX() < object.getX()) {
                player.addWalkSteps(player.getX() + 2, player.getY(), -1, false);
            } else {
                player.addWalkSteps(player.getX() - 2, player.getY(), -1, false);
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MAGICAL_BARRIER_31617 };
    }
}
