package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KharaziRocks implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.sendMessage("There's nothing behind the rocks.");
        //Real coordinates: 2772, 9341, 0
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.MOSSY_ROCK_2901, ObjectId.MOSSY_ROCK, ObjectId.ROCKS_2902
        };
    }
}
