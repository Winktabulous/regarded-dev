package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/04/2019 02:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class IceQuestCavern implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.sendMessage("You already defeated the ice troll king!");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_21584, ObjectId.CAVE_21585 };
    }
}
