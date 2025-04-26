package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.game.util.Direction;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/06/2019 14:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
class MapChunk {
    MapChunk(@NotNull final RaidRoom room) {
        this.room = room;
    }

    MapChunk(@NotNull final LayoutRoom room) {
        this.room = room.getRoom();
        final Direction dir = room.getDirection();
        direction = dir == Direction.NORTH ? ChunkDirection.NORTH : dir == Direction.SOUTH ? ChunkDirection.SOUTH : dir == Direction.WEST ? ChunkDirection.WEST : ChunkDirection.EAST;
    }

    @NotNull
    final RaidRoom room;
    int x;
    int y;
    ChunkDirection direction;
    int chunkDirection;

    @Override
    public String toString() {
        return "MapChunk(room=" + this.room + ", x=" + this.x + ", y=" + this.y + ", direction=" + this.direction + ", chunkDirection=" + this.chunkDirection + ")";
    }
}
