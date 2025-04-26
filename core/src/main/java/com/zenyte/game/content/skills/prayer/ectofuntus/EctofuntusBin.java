package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 23/06/2019 13:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EctofuntusBin implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Empty")) {
            player.getActionManager().setAction(new BoneGrinding(BoneGrinding.Stage.COLLECTING, null));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BIN };
    }
}
