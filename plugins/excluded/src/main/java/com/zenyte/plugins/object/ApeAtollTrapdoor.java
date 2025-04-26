package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 14:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ApeAtollTrapdoor implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Open")) {
            player.sendMessage("You can't open this trapdoor.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TRAPDOOR_4712 };
    }
}
