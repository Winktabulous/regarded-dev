package com.zenyte.game.content.minigame.warriorsguild.plugins;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 23/03/2019 17:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CatapultControls implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("View")) {
            player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 410);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.INFORMATION_SCROLL_24909 };
    }
}
