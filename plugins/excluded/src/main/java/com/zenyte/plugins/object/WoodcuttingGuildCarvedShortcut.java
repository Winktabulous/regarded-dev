package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14/11/2018 21:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WoodcuttingGuildCarvedShortcut implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setLocation(new Location(player.getX(), player.getY(), (object.getId() == 29681 ? 1 : -1) + player.getPlane()));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CARVED_REDWOOD_29681, ObjectId.CARVED_REDWOOD_29682 };
    }

    @Override
    public int getDelay() {
        return 1;
    }
}
