package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class SlayerDungeonStairs implements ObjectAction {

    private static final Location FREM_TOP = new Location(2703, 9989, 0);

    private static final Location FREM_BOTTOM = new Location(2703, 9991, 0);

    private static final Location WYVERN_TOP = new Location(3060, 9557, 0);

    private static final Location WYVERN_BOTTOM = new Location(3060, 9555, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Climb")) {
            // true if in fremmenik slayer cave
            final boolean location = object.getId() == 29993;
            // top going bottom
            final boolean top = location ? player.getY() < object.getY() : player.getY() > object.getY();
            final Location finish = location ? (top ? FREM_BOTTOM : FREM_TOP) : (top ? WYVERN_BOTTOM : WYVERN_TOP);
            player.setFaceLocation(finish);
            player.addWalkSteps(finish.getX(), finish.getY(), -1, false);
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STEPS_29993, ObjectId.STEPS };
    }
}
