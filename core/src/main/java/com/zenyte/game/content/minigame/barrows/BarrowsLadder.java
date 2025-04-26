package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 30/11/2018 23:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarrowsLadder implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Climb-up")) {
            final Barrows barrows = player.getBarrows();
            final BarrowsWight hiddenWight = barrows.getHiddenWight();
            player.setLocation(hiddenWight.getBySarcophagus());
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_20673, 20674, 20675, 20676, 20677 };
    }
}
