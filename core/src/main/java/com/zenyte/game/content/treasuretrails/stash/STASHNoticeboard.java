package com.zenyte.game.content.treasuretrails.stash;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 29/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class STASHNoticeboard implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.STASH_UNIT.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.NOTICEBOARD_29718 };
    }
}
