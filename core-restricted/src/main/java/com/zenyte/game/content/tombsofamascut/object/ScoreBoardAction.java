package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions
 */
public class ScoreBoardAction implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.TOA_SCOREBOARD.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {44942};
    }
}
