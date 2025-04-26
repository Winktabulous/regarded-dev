package com.zenyte.game.content.boss.bryophyta.plugins;

import com.zenyte.game.content.boss.bryophyta.BryophytaInstance;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 17/05/2019 | 15:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BryophytaExitObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setLocation(BryophytaInstance.OUTSIDE_INSTANCE);
        player.sendMessage("Cautiously, you climb out of the damp cave.");
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROCK_PILE_32535 };
    }
}
