package com.zenyte.game.model;

/**
 * A class that positions non-entity hint arrows to a certain location in the given tile.
 * @author Kris | 11. dets 2017 : 23:56.31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum HintArrowPosition {

    ENTITY(1),
    CENTER(2),
    WEST(3),
    EAST(4),
    SOUTH(5),
    NORTH(6);

    private final int positionHash;

    HintArrowPosition(final int positionHash) {
        this.positionHash = positionHash;
    }

    public int getPositionHash() {
        return positionHash;
    }

}
