package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 28/04/2019 19:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CentreOfCropCircle implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Enter")) {
            TeleportCollection.PURO_CENTER_OF_CROP_CIRCLE.teleport(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CENTRE_OF_CROP_CIRCLE, ObjectId.CENTRE_OF_CROP_CIRCLE_24991 };
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }
}
