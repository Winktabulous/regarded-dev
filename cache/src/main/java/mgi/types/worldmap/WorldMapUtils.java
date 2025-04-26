package mgi.types.worldmap;

import com.zenyte.CacheManager;
import com.zenyte.game.world.region.XTEALoader;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.ByteBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 11/12/2018 20:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class WorldMapUtils {

    public static WorldMapGameObject[][][][] getWorldMapObjects(final int regionId, int plane) throws IOException {
        final Cache cache = CacheManager.getCache();
        final Archive archive = cache.getArchive(ArchiveType.MAPS);
        final int[] xteas = XTEALoader.getXTEAs(regionId);
        final Group landGroup = archive.findGroupByName("l" + (regionId >> 8) + "_" + (regionId & 255), xteas);
        if (landGroup == null) {
            throw new RuntimeException("Map file is null!");
        }
        final ByteBuffer buffer = landGroup.findFileByID(0).getData();
        final WorldMapGameObject[][][][] objects = new WorldMapGameObject[4][64][64][];
        int lastX = 0;
        int lastY = 0;
        int lastZ = 0;
        List<WorldMapGameObject> list = new ArrayList<>(4);
        final int[][][] settings = getClipSettings(regionId);
        int objectId = -1;
        int objectIdDifference;
        buffer.setPosition(0);
        while ((objectIdDifference = buffer.readHugeSmart()) != 0) {
            objectId += objectIdDifference;
            int location = 0;
            int locationHashDifference;
            while ((locationHashDifference = buffer.readUnsignedSmart()) != 0) {
                location += locationHashDifference - 1;
                final int hash = buffer.readUnsignedByte();
                final int x = (location >> 6 & 63);
                final int y = (location & 63);
                int realZ = location >> 12 & 3;
                if ((settings[1][x][y] & 2) == 2) {
                    realZ--;
                }
                if (realZ != plane) continue;
                final int type = hash >> 2;
                final int rotation = hash & 3;
                if (lastX != x || lastY != y || lastZ != realZ) {
                    if (!list.isEmpty()) {
                        objects[lastZ - plane][lastX][lastY] = list.toArray(new WorldMapGameObject[0]);
                        list.clear();
                    }
                    lastX = x;
                    lastY = y;
                    lastZ = realZ;
                }
                final ObjectDefinitions definitions = ObjectDefinitions.get(objectId);
                /*if (definitions == null) {
                    throw new RuntimeException("Object id " + objectId + " does not exist.");
                }*/
                if (definitions != null) {
                    if (definitions.getMapSceneId() != -1 || definitions.getMapIconId() != -1 || type == 0 || type == 2 || type == 3 || type == 9) {
                        list.add(new WorldMapGameObject(objectId, type, rotation));
                    }
                }
            }
        }
        if (!list.isEmpty()) {
            objects[lastZ][lastX][lastY] = list.toArray(new WorldMapGameObject[0]);
        }
        return objects;
    }

    public static OverlayInformation getOverlays(final int regionId, int baseHeight) throws IOException {
        final Cache cache = CacheManager.getCache();
        final Archive archive = cache.getArchive(ArchiveType.MAPS);
        final Group mapGroup = archive.findGroupByName("m" + (regionId >> 8) + "_" + (regionId & 255));
        if (mapGroup == null) {
            throw new RuntimeException("Map file is null!");
        }
        final ByteBuffer buffer = mapGroup.findFileByID(0).getData();
        buffer.setPosition(0);
        short[][][] ids = new short[4 - baseHeight][64][64];
        byte[][][] rotations = new byte[4 - baseHeight][64][64];
        byte[][][] shapes = new byte[4 - baseHeight][64][64];
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    while (true) {
                        final int attribute = buffer.readUnsignedShort();
                        if (attribute == 0) {
                            break;
                        } else if (attribute == 1) {
                            buffer.readByte();
                            break;
                        } else if (attribute <= 49) {
                            final short id = (short) buffer.readShort();
                            if (z < baseHeight) continue;
                            ids[z - baseHeight][x][y] = id;
                            shapes[z - baseHeight][x][y] = (byte) ((attribute - 2) / 4);
                            rotations[z - baseHeight][x][y] = (byte) (attribute - 2 & 3);
                        } else if (attribute <= 81) {
                            //settings[z - baseHeight][x][y] = (byte) (attribute - 49);
                        } else {

                        }
                    }
                }
            }
        }
        final int[][][] settings = getClipSettings(regionId);
        for (int z = 0; z < 3 - baseHeight; z++) {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    if ((settings[1][x][y] & 2) == 2) {
                        if (z <= 0) continue;
                        ids[z][x][y] = ids[z - 1][x][y];
                        shapes[z][x][y] = shapes[z - 1][x][y];
                        rotations[z][x][y] = rotations[z - 1][x][y];
                    }
                }
            }
        }
        return new OverlayInformation(ids, shapes, rotations);
    }

    public static int[][][] getClipSettings(final int regionId) throws IOException {
        final Cache cache = CacheManager.getCache();
        final Archive archive = cache.getArchive(ArchiveType.MAPS);
        final Group mapGroup = archive.findGroupByName("m" + (regionId >> 8) + "_" + (regionId & 255));
        if (mapGroup == null) {
            throw new RuntimeException("Map file is null!");
        }
        final ByteBuffer buffer = mapGroup.findFileByID(0).getData();
        buffer.setPosition(0);
        final int[][][] settings = new int[4][64][64];
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    while (true) {
                        final int attribute = buffer.readUnsignedByte();
                        if (attribute == 0) {
                            break;
                        } else if (attribute == 1) {
                            buffer.readByte();
                            break;
                        } else if (attribute <= 49) {
                            buffer.readShort();
                        } else if (attribute <= 81) {
                            settings[z][x][y] = (byte) (attribute - 49);
                        } else {
                            // underlay id
                        }
                    }
                }
            }
        }
        return settings;
    }

    public static short[][][] getUnderlays(final int regionId, int baseHeight) throws IOException {
        final Cache cache = CacheManager.getCache();
        final Archive archive = cache.getArchive(ArchiveType.MAPS);
        final Group mapGroup = archive.findGroupByName("m" + (regionId >> 8) + "_" + (regionId & 255));
        if (mapGroup == null) {
            throw new RuntimeException("Map file is null!");
        }
        final ByteBuffer buffer = mapGroup.findFileByID(0).getData();
        buffer.setPosition(0);
        final int increment = 1;
        final short[][][] underlays = new short[4 - baseHeight][64][64];
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 64; x += increment) {
                for (int y = 0; y < 64; y += increment) {
                    while (true) {
                        final int attribute = buffer.readUnsignedShort();
                        if (attribute == 0) {
                            break;
                        } else if (attribute == 1) {
                            buffer.readByte();
                            break;
                        } else if (attribute <= 49) {
                            buffer.readShort(); // overlay id
                        } else if (attribute <= 81) {
                            // settings
                        } else {
                            if (z < baseHeight) continue;
                            underlays[z - baseHeight][x][y] = (short) (attribute - 81);
                        }
                    }
                }
            }
        }
        return underlays;
    }

    public static final class OverlayInformation {
        private final short[][][] ids;
        private final byte[][][] shapes;
        private final byte[][][] rotations;

        public OverlayInformation(short[][][] ids, byte[][][] shapes, byte[][][] rotations) {
            this.ids = ids;
            this.shapes = shapes;
            this.rotations = rotations;
        }

        public short[][][] getIds() {
            return ids;
        }

        public byte[][][] getShapes() {
            return shapes;
        }

        public byte[][][] getRotations() {
            return rotations;
        }
    }

}
