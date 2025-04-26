package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 01/10/2019 | 21:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class NieveGravestone implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Read")) {
            GameInterface.NIEVE_GRAVESTONE.open(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 28722 };
    }
}
