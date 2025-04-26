package com.zenyte.game.world.region;

import com.zenyte.game.world.object.WorldObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 18. veebr 2018 : 3:11.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DynamicChunk {

    private final int regionId, x, y, plane, rotation;
    private int[][][] masks;
    private Map<Byte, WorldObject> objects;

    public DynamicChunk(final int regionId, final int x, final int y, final int plane, final int rotation) {
        this.regionId = regionId;
        this.x = x;
        this.y = y;
        this.plane = plane;
        this.rotation = rotation;
        this.objects = new HashMap<Byte, WorldObject>();
        this.masks = new int[4][8][8];
    }

    public final DynamicChunk getRotatedChunk(final int rotation) {
        final DynamicChunk chunk = new DynamicChunk(regionId, x, y, plane, rotation);
        chunk.objects = new HashMap<Byte, WorldObject>(this.objects);
        final int[][][] masks = new int[4][8][8];
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    masks[z][x][y] = this.masks[z][x][y];
                }
            }
        }
        chunk.masks = masks;
        return chunk;
    }

    @Override
    public int hashCode() {
        return x | y << 11 | plane << 22;
    }

    public int getRegionId() {
        return regionId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlane() {
        return plane;
    }

    public int getRotation() {
        return rotation;
    }

    public int[][][] getMasks() {
        return masks;
    }

    public Map<Byte, WorldObject> getObjects() {
        return objects;
    }

}
