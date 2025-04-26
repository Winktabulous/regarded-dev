package com.zenyte.game.content.minigame.barrows;

import static com.zenyte.game.content.minigame.barrows.CryptDoorway.*;

/**
 * @author Kris | 28/11/2018 21:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum CryptRoom {

    NORTH_WEST_ROOM(NORTH_LONG_PASSAGE, WEST_LONG_PASSAGE, NORTH_WEST_TO_WEST, NORTH_TO_NORTH_WEST),
    WEST_ROOM(NORTH_WEST_TO_WEST, WEST_TO_SOUTH_WEST),
    SOUTH_WEST_ROOM(WEST_LONG_PASSAGE, SOUTH_LONG_PASSAGE, WEST_TO_SOUTH_WEST, SOUTH_WEST_TO_SOUTH),
    SOUTH_ROOM(SOUTH_WEST_TO_SOUTH, SOUTH_TO_SOUTH_EAST),
    SOUTH_EAST_ROOM(SOUTH_LONG_PASSAGE, EAST_LONG_PASSAGE, SOUTH_TO_SOUTH_EAST, SOUTH_EAST_TO_EAST),
    EAST_ROOM(SOUTH_EAST_TO_EAST, EAST_TO_NORTH_EAST),
    NORTH_EAST_ROOM(NORTH_LONG_PASSAGE, EAST_LONG_PASSAGE, EAST_TO_NORTH_EAST, NORTH_EAST_TO_NORTH),
    NORTH_ROOM(NORTH_TO_NORTH_WEST, NORTH_EAST_TO_NORTH);

    public static final CryptRoom[] values = values();

    final CryptDoorway[] doorways;

    CryptRoom(final CryptDoorway... doorways) {
        this.doorways = doorways;
    }

}
