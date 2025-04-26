package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 28/11/2018 21:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum BarrowsCorner {

    SOUTH_WEST(CryptRoom.SOUTH_WEST_ROOM, new Location(3534, 9678, 0)),
    NORTH_WEST(CryptRoom.NORTH_WEST_ROOM, new Location(3534, 9712, 0)),
    NORTH_EAST(CryptRoom.NORTH_EAST_ROOM, new Location(3568, 9712, 0)),
    SOUTH_EAST(CryptRoom.SOUTH_EAST_ROOM, new Location(3568, 9678, 0));

    final CryptRoom room;
    final Location ladder;

    static BarrowsCorner[] values = values();

    BarrowsCorner(CryptRoom room, Location ladder) {
        this.room = room;
        this.ladder = ladder;
    }

}
