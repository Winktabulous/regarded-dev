package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KaramjaCave implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.sendMessage("The cave has collapsed - you see no way in.");
        //Actual coords: 2847, 9292
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.CAVE_32479
        };
    }
}
