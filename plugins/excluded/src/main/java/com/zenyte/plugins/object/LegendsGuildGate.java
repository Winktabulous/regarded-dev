package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ForcedGate;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.Optional;

/**
 * @author Kris | 10/05/2019 18:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LegendsGuildGate implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("open")) {
            new ForcedGate<>(player, object).handle(Optional.empty());
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_2391, ObjectId.GATE_2392 };
    }
}
