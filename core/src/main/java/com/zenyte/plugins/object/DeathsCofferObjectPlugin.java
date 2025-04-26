package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14/06/2022
 */
public class DeathsCofferObjectPlugin implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.DEATHS_OFFICE_SACRIFICE.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 39550 };
    }
}
