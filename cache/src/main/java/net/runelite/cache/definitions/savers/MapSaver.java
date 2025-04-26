//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.cache.definitions.savers;

import net.runelite.cache.definitions.MapDefinition;
import net.runelite.cache.io.OutputStream;

public class MapSaver {
    public MapSaver() {
    }

    public byte[] save(MapDefinition map) {
        MapDefinition.Tile[][][] tiles = map.getTiles();
        OutputStream out = new OutputStream();

        for(int z = 0; z < 4; ++z) {
            for(int x = 0; x < 64; ++x) {
                for(int y = 0; y < 64; ++y) {
                    MapDefinition.Tile tile = tiles[z][x][y];
                    if (tile.attrOpcode != 0) {
                        out.writeShort(tile.attrOpcode);
                        out.writeShort(tile.overlayId);
                    }

                    if (tile.settings != 0) {
                        out.writeShort(tile.settings + 49);
                    }

                    if (tile.underlayId != 0) {
                        out.writeShort(tile.underlayId + 81);
                    }

                    if (tile.height == null) {
                        out.writeShort(0);
                    } else {
                        out.writeShort(1);
                        out.writeByte(tile.height);
                    }
                }
            }
        }

        return out.flip();
    }
}
