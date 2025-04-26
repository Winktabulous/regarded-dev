package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.utils.Ordinal;

/**
 * @author Kris | 28/06/2019 14:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */

@Ordinal
public enum ChunkDirection {
    NORTH(0, 1),
    EAST(1, 0),
    SOUTH(0, -1),
    WEST(-1, 0);

    static final ChunkDirection[] values = values();
    private final int xOffset, yOffset;

    ChunkDirection(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

}
