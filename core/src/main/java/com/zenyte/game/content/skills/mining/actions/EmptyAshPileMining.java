package com.zenyte.game.content.skills.mining.actions;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 22/05/2019 14:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EmptyAshPileMining implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Mine")) {
            player.sendMessage("There's not enough ash in this pile for you to mine.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.EMPTY_ASH_PILE };
    }
}
