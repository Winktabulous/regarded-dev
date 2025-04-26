package com.zenyte.game.content.minigame.barrows;

/**
 * @author Kris | 29/11/2018 13:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum CryptDoorway {

    EAST_TO_CENTER(478),
    SOUTH_TO_CENTER(480),
    WEST_TO_CENTER(477),
    NORTH_TO_CENTER(473),

    NORTH_LONG_PASSAGE(469),
    WEST_LONG_PASSAGE(470),
    SOUTH_LONG_PASSAGE(484),
    EAST_LONG_PASSAGE(476),
    
    NORTH_WEST_TO_WEST(471),
    WEST_TO_SOUTH_WEST(479),
    SOUTH_WEST_TO_SOUTH(482),
    SOUTH_TO_SOUTH_EAST(483),
    SOUTH_EAST_TO_EAST(481),
    EAST_TO_NORTH_EAST(475),
    NORTH_EAST_TO_NORTH(474),
    NORTH_TO_NORTH_WEST(472);

    static final CryptDoorway[] values = values();

    CryptDoorway(final int varbitId) {
        this.varbitId = varbitId;
    }

    final int varbitId;

    static final CryptDoorway[] centerDoorways = new CryptDoorway[] {
            EAST_TO_CENTER, SOUTH_TO_CENTER, WEST_TO_CENTER, NORTH_TO_CENTER
    };
}
