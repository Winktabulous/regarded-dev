package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 06/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WestArdougneGate implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        new FadeScreen(player, () -> player.setLocation(player.getX() >= 2559 ? new Location(2556, player.getY(), 0) : new Location(2559, player.getY(), 0))).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ARDOUGNE_WALL_DOOR, ObjectId.ARDOUGNE_WALL_DOOR_8739 };
    }
}
