package com.zenyte.game.content.treasuretrails;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 04/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TreasureTrailStatisticsObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.TREASURE_TRAIL_STATISTICS.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TREASURE_TRAIL_STATISTICS_26634, ObjectId.TREASURE_TRAIL_STATISTICS };
    }
}
