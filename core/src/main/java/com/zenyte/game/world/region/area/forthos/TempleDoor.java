package com.zenyte.game.world.region.area.forthos;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public final class TempleDoor implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == 34842) {
            player.sendMessage("You don't have a key which can open this door.");
        } else {
            if (player.getX() == 1824 && player.getY() == 9973) {
                player.sendMessage("The door slams shut and locks behind you.");
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34842, 34843 };
    }

}
