package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 02/05/2019 | 21:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LithkrenVaultBarrierObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock(2);
        player.addWalkSteps(object.getX() + (Integer.compare(object.getX(), player.getX())), object.getY() + (Integer.compare(object.getY(), player.getY())), 2, false);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BARRIER_32153 };
    }
}
