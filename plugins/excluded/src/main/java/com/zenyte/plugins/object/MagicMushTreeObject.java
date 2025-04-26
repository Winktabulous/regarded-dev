package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 19/07/2019 | 16:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class MagicMushTreeObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Use")) {
            GameInterface.MYCELIUM_TELEPORTATION.open(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Magic Mushtree" };
    }
}
