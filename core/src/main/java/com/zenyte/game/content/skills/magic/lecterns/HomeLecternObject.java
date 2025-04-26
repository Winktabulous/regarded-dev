package com.zenyte.game.content.skills.magic.lecterns;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 03/09/2019 08:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HomeLecternObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        GameInterface.REGULAR_LECTERN.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LECTERN_35008 };
    }
}
