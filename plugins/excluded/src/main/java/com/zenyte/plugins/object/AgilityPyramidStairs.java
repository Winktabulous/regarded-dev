package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.agility.pyramid.area.AgilityPyramidArea;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class AgilityPyramidStairs implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == ObjectId.STAIRS_10857) {
            player.setLocation(AgilityPyramidArea.getHigherTile(player.getLocation().transform(Direction.NORTH, 3)));
        } else {
            player.setLocation(AgilityPyramidArea.getLowerTile(player.getLocation().transform(Direction.SOUTH, 3)));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRS_10857, ObjectId.STAIRS_10858 };
    }
}
