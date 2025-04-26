package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import mgi.custom.halloween.HalloweenObject;

/**
 * @author Kris | 04/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HalloweenJumpableObject implements ObjectAction {
    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Jump-over")) {
            final Direction dir = player.getX() < object.getX() ? Direction.EAST : player.getX() > object.getX() ? Direction.WEST : player.getY() > object.getY() ? Direction.SOUTH : Direction.NORTH;
            final Location destination = player.getLocation().transform(dir, 2);
            if (object.getId() != HalloweenObject.PILE_OF_RUBBLE.getRepackedObject() && player.isProjectileClipped(destination, false)) {
                player.sendMessage("You can't jump that way.");
                return;
            }
            player.lock(2);
            player.setAnimation(new Animation(1603));
            player.autoForceMovement(destination, 0, 30);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {HalloweenObject.STRANGE_FLOOR.getRepackedObject(), HalloweenObject.PILE_OF_RUBBLE.getRepackedObject()};
    }
}
