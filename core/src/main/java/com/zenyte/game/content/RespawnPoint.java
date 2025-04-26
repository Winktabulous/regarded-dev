package com.zenyte.game.content;

import com.zenyte.game.world.entity.Location;

/**
 * @author Tommeh | 14 jan. 2018 : 21:02:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum RespawnPoint {

    LUMBRIDGE(new Location(3224, 3219, 0)),
    FALADOR(new Location(2973, 3340, 0)),
    CAMELOT(new Location(2757, 3477, 0)),
    ARDOUGNE(new Location(2674, 3291, 0)),
    EDGEVILLE(new Location(3106, 3493, 0));

    private final Location location;

    RespawnPoint(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}
