package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 23/04/2019 23:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MythsGuildBarrier implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.lock(2);
        player.setRunSilent(2);
        if (player.getY() <= 2860) {
            player.addWalkSteps(player.getX(), 2862, 2, false);
        } else {
            player.addWalkSteps(player.getX(), 2860, 2, false);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MAGICAL_BARRIER };
    }
}
