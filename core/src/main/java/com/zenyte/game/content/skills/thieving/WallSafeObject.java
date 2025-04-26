package com.zenyte.game.content.skills.thieving;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24/03/2019 20:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WallSafeObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Crack")) {
            player.setFaceLocation(new Location(object.getX(), object.getY() + (player.getY() >= 4977 ? -1 : 1)));
            player.getActionManager().setAction(new WallSafe(object));
        }
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.WALL_SAFE };
    }
}
