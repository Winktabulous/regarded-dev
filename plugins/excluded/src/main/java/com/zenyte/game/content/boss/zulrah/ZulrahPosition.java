package com.zenyte.game.content.boss.zulrah;

import com.zenyte.game.world.entity.Location;

/**
 * An enum containing the static locations to Zulrah spawn and face coordinates.
 *
 * @author Kris | 15. march 2018 : 17:42.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum ZulrahPosition {

    SOUTH(new Location(2266, 3062, 0), new Location(2268, 3068, 0)),
    WEST(new Location(2257, 3071, 0), new Location(2268, 3073, 0)),
    CENTER(new Location(2266, 3073, 0), new Location(2268, 3065, 0)),
    EAST(new Location(2276, 3072, 0), new Location(2270, 3074, 0));

    private final Location spawn, face;

    ZulrahPosition(final Location spawn, final Location face) {
        this.spawn = spawn;
        this.face = face;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Location getFace() {
        return face;
    }

}
