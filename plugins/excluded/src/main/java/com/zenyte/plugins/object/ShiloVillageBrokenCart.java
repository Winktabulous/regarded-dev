package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/03/2019 19:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShiloVillageBrokenCart implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Location dest = player.getX() > 2877 ? new Location(2876, 2952, 0) : new Location(2880, 2951, 0);
        new FadeScreen(player, () -> player.setLocation(dest)).fade(2);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BROKEN_CART_28931, 2216 };
    }
}
