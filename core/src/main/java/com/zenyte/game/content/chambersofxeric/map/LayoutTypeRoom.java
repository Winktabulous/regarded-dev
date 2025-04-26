package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.game.util.Direction;

/**
 * @author Kris | 21/09/2019 22:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class LayoutTypeRoom {
    private final RoomType type;
    private final Direction direction;

    public LayoutTypeRoom(RoomType type, Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public RoomType getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }
}
