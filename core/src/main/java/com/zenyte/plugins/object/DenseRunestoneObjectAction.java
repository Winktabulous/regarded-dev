package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.mining.actions.DenseRunestoneMining;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class DenseRunestoneObjectAction  implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new DenseRunestoneMining(object));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DENSE_RUNESTONE };
    }
}
