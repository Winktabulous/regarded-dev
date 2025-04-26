package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import mgi.custom.halloween.HalloweenObject;
import mgi.utilities.CollectionUtils;

/**
 * @author Kris | 05/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HalloweenSteppingStone implements ObjectAction {
    private static final Location[] permittedLocations = new Location[] {new Location(1748, 4709, 0), new Location(1749, 4710, 0), new Location(1750, 4709, 0), new Location(1751, 4709, 0), new Location(1752, 4710, 0)};

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (CollectionUtils.findMatching(permittedLocations, object::matches) != null) {
            if (player.getLocation().getTileDistance(object) == 1) {
                if (player.getX() >= 1748 && player.getX() <= 1753 && player.getY() >= 4709 && player.getY() <= 4710) {
                    getRunnable(player, object, name, optionId, option).run();
                    return;
                }
            }
        }
        player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Cross")) {
            final Direction dir = player.getX() < object.getX() && player.getY() < object.getY() ? Direction.NORTH_EAST : player.getX() < object.getX() && player.getY() > object.getY() ? Direction.SOUTH_EAST : player.getX() > object.getX() && player.getY() < object.getY() ? Direction.NORTH_WEST : player.getX() > object.getX() && player.getY() > object.getY() ? Direction.SOUTH_WEST : player.getX() < object.getX() ? Direction.EAST : player.getX() > object.getX() ? Direction.WEST : player.getY() > object.getY() ? Direction.SOUTH : Direction.NORTH;
            final Location destination = player.getLocation().transform(dir, 1);
            if (player.isProjectileClipped(destination, false)) {
                player.sendMessage("You can't jump that way.");
                return;
            }
            player.lock(2);
            player.setAnimation(Animation.JUMP);
            player.autoForceMovement(destination, 0, 30);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {HalloweenObject.STEPPING_STONES.getRepackedObject()};
    }
}
