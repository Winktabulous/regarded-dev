package com.zenyte.game.world.region.area.forthos;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Andys1814
 */
public final class ForthosExit implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == 34864) {
            player.teleport(new Location(1671, 3567));
        } else {
            player.setAnimation(Animation.LADDER_UP);
            WorldTasksManager.schedule(() -> {
                player.teleport(new Location(1702, 3575));
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34864, 34863 };
    }

}
