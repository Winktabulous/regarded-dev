//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.cache.definitions.loaders;

import net.runelite.cache.definitions.MapDefinition;
import net.runelite.cache.io.InputStream;

public class MapLoaderPre209 {
    public MapLoaderPre209() {
    }

    public MapDefinition load(int regionX, int regionY, byte[] b) {
        MapDefinition map = new MapDefinition();
        map.setRegionX(regionX);
        map.setRegionY(regionY);
        this.loadTerrain(map, b);
        return map;
    }

    private void loadTerrain(MapDefinition map, byte[] buf) {
        MapDefinition.Tile[][][] tiles = map.getTiles();
        InputStream in = new InputStream(buf);

        for(int z = 0; z < 4; ++z) {
            for(int x = 0; x < 64; ++x) {
                for(int y = 0; y < 64; ++y) {
                    MapDefinition.Tile tile = tiles[z][x][y] = new MapDefinition.Tile();

                    while(true) {
                        int attribute = in.readUnsignedByte();
                        if (attribute == 0) {
                            break;
                        }

                        if (attribute == 1) {
                            int height = in.readUnsignedByte();
                            tile.height = height;
                            break;
                        }

                        if (attribute <= 49) {
                            tile.attrOpcode = attribute;
                            tile.overlayId = (short)in.readUnsignedByte();
                            tile.overlayPath = (byte)((attribute - 2) / 4);
                            tile.overlayRotation = (byte)(attribute - 2 & 3);
                        } else if (attribute <= 81) {
                            tile.settings = (byte)(attribute - 49);
                        } else {
                            tile.underlayId = (short)(attribute - 81);
                        }
                    }
                }
            }
        }

    }
}
