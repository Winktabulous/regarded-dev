package com.zenyte.game.content.magiccarpet;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.ForceMovement;

/**
 * @author Kris | 21. aug 2018 : 12:35:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class CarpetMovement {

    private final ForceMovement forceMovement;
    private final Location steps;

    public CarpetMovement(final Location location) {
        steps = location;
        forceMovement = null;
    }
    public CarpetMovement(final Location location, final int delay, final int direction) {
        forceMovement = new ForceMovement(location, delay, direction);
        steps = null;
    }

    public ForceMovement getForceMovement() {
        return forceMovement;
    }

    public Location getSteps() {
        return steps;
    }

}
