package com.near_reality.game.content.gauntlet.map;

import com.zenyte.game.util.Utils;

/**
 * An enumerated type whose elements correspond to the possible types of rooms in a Gauntlet map grid.
 *
 * @author Andys1814
 * @since 1/22/2022.
 */
public enum GauntletRoomType {
    MIDDLE(232), // Rooms with four entrances that appear in the middle of the Gauntlet grid.
    EDGE(234), // Rooms with three entrances that appear on the four sides of the Gauntlet grid.
    CORNER(236); // Rooms with two entrances that appear in the middle of the Gauntlet grid.

    private final int staticChunkX;

    GauntletRoomType(int staticBaseChunkX) {
        this.staticChunkX = staticBaseChunkX;
    }

    public int getStaticChunkX() {
        return staticChunkX;
    }

    public int getCorruptedStaticChunkX() {
        return staticChunkX + 8;
    }

    public int getRandomStaticChunkY() {
        return 704 + (2 * Utils.random(0, 3));
    }

}
