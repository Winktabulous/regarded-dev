package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.FairyRing;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.interfaces.FairyRingCombination;
import com.zenyte.plugins.interfaces.FairyRingLog;

/**
 * @author Kris | 10. nov 2017 : 22:14.39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class FairyRingObject implements ObjectAction {

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Runnable runnable = () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        };
        if (player.getLocation().getPositionHash() == object.getPositionHash()) {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(player.getLocation())), runnable));
        } else {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), runnable));
        }
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Zanaris") || option.equals("Use") && object.getId() == 14839) {
            FairyRing.handle(player, object, FairyRing.codes.get("BKS"));
        }
        if (option.equals("Configure")) {
            FairyRingCombination.open(player, object);
            FairyRingLog.open(player);
        }
        if (option.startsWith("Last-destination")) {
            final Object obj = player.getNumericAttribute("lastFairyRing");
            if (!(obj instanceof Integer)) {
                player.sendFilteredMessage("You haven't used the fairy ring teleportation system yet.");
                return;
            }
            final int last = (int) obj;
            FairyRing.handle(player, object, FairyRing.getRing(last));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 14839, 29495, 29560 };
    }
}
