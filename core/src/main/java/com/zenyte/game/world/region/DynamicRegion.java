package com.zenyte.game.world.region;

import com.zenyte.CacheManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.CoordinateUtilities;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 28. juuli 2018 : 18:39:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class DynamicRegion extends Region {
    private static final Logger log = LoggerFactory.getLogger(DynamicRegion.class);
    private static final int CHUNKS_PER_REGION = 256;
    private static final int EXPECTED_CHUNK_CAPACITY = 1000;
    private static final byte IS_TILE_CLIPPED_FLAG = 1;
    private static final byte IS_TILE_INCREASED_HEIGHT_FLAG = 2;
    private static final Int2ObjectMap<InnerChunk> chunkMap =
            new Int2ObjectOpenHashMap<>(EXPECTED_CHUNK_CAPACITY);
    private final ByteOpenHashSet reloadChunkList;
    private final Byte2IntOpenHashMap chunks;

    public DynamicRegion(final int regionId) {
        super(regionId);

        chunks = new Byte2IntOpenHashMap(CHUNKS_PER_REGION);
        reloadChunkList = new ByteOpenHashSet(CHUNKS_PER_REGION);
        for (int i = 0; i < CHUNKS_PER_REGION; i++) {
            reloadChunkList.add((byte) i);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        reloadChunkList.clear();
        chunks.clear();
    }

    @Override
    public void load() {
        if (!reloadChunkList.isEmpty()) {
            setLoadStage(0);
        }
        super.load();
    }

    @Override
    public void loadRegionMap() {
        final ByteIterator iterator = reloadChunkList.iterator();
        final RegionMap map = getRegionMap(true);
        final int baseX = (regionId >> 8) << 6;
        final int baseY = (regionId & 255) << 6;
        while (iterator.hasNext()) {
            /* Next hash points to the chunk within this region that has been updated. */
            final byte nextHash = iterator.nextByte();
            final int chunkHash = chunks.get(nextHash);
            if (chunkHash == 0) {
                continue;
            }
            final int chunkXInRegion = (nextHash & 7) << 3;
            final int chunkYInRegion = ((nextHash >> 3) & 7) << 3;
            final int z = (nextHash >> 6) & 3;
            /* From chunk hash contains a hashcode pointer to the chunk that we're copying. */
            final int fromChunkHash = chunks.get(nextHash);
            if (!chunkMap.containsKey(fromChunkHash & 4194303)) {
                loadRegionFromChunk(fromChunkHash & 4194303);
            }
            try {
                final int chunkX = chunkHash & 2047;
                final int chunkY = (chunkHash >> 11) & 2047;
                final int regionId = (chunkX >> 3) << 8 | (chunkY >> 3);
                getMusicTracks().addAll(World.getRegion(regionId).getMusicTracks());
            } catch (Exception e) {
                log.error("", e);
            }
            final DynamicRegion.InnerChunk innerChunk = chunkMap.get(fromChunkHash & 4194303);
            if (innerChunk != null) {
                final int fromPlane = (fromChunkHash >> 22) & 3;
                final int rotation = (fromChunkHash >> 24) & 3;
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        final byte hash = getChunkHashInRegion(x, y, fromPlane);
                        final byte tileHash = innerChunk.fullyClipped ? IS_TILE_CLIPPED_FLAG :
                                innerChunk.clipSettings.get(hash);
                        final List<DynamicRegion.DynamicObject> objects = innerChunk.objects.get(hash);
                        int plane = z;
                        final byte firstLevelHash = innerChunk.fullyClipped ? IS_TILE_CLIPPED_FLAG :
                                innerChunk.clipSettings.get(getChunkHashInRegion(x, y, 1));
                        if ((firstLevelHash & IS_TILE_INCREASED_HEIGHT_FLAG) == IS_TILE_INCREASED_HEIGHT_FLAG) {
                            plane--;
                        }
                        if (plane < 0 || plane > 3) continue;
                        if ((tileHash & IS_TILE_CLIPPED_FLAG) == IS_TILE_CLIPPED_FLAG) {
                            final int[] coordinates = CoordinateUtilities.translate(x, y, rotation);
                            final int xInRegion = chunkXInRegion + coordinates[0];
                            final int yInRegion = chunkYInRegion + coordinates[1];
                            map.setFloor(plane, xInRegion, yInRegion, true);
                        }
                        if (objects != null) {
                            for (final DynamicRegion.DynamicObject object : objects) {
                                final ObjectDefinitions definitions = ObjectDefinitions.get(object.getId());
                                if (definitions == null) continue;
                                final int[] coordinates = CoordinateUtilities.translate(x, y, rotation,
                                        definitions.getSizeX(), definitions.getSizeY(), object.getRotation());
                                final int xInRegion = chunkXInRegion + coordinates[0];
                                final int yInRegion = chunkYInRegion + coordinates[1];
                                final WorldObject obj = new WorldObject(object.getId(), object.getType(),
                                        (rotation + object.getRotation()) & 3, xInRegion + baseX, yInRegion + baseY,
                                        plane);
                                if (obj.getRegionId() != regionId) {
                                    World.getRegion(obj.getRegionId(), true).spawnObject(obj, plane,
                                            obj.getXInRegion(), obj.getYInRegion(), true, true);
                                    continue;
                                }
                                spawnObject(obj, plane, obj.getXInRegion(), obj.getYInRegion(), true, true);
                            }
                        }
                    }
                }
            }
        }
        reloadChunkList.clear();
    }

    public static int getHash(final int x, final int y, final int z, final int rotation) {
        return x | y << 11 | z << 22 | rotation << 24;
    }

    public void setChunk(final int x, final int y, final int z, final int hash) {
        final byte mapHash = (byte) (x & 7 | (y & 7) << 3 | (z & 3) << 6);
        if (hash == 0) {
            chunks.remove(mapHash);
        } else {
            chunks.put(mapHash, hash);
        }
        reloadChunkList.add(mapHash);
    }

    public int getLocationHash(final int x, final int y, final int z) {
        return chunks.get((byte) (x | y << 3 | z << 6));
    }

    public void clearMultiZones() {
        final ByteSet keyset = chunks.keySet();
        final ByteIterator iterator = keyset.iterator();
        while (iterator.hasNext()) {
            final byte nextHash = iterator.nextByte();
            final int chunkX = nextHash & 7;
            final int chunkY = (nextHash >> 3) & 7;
            final int z = (nextHash >> 6) & 3;
            removeMultiZone(((regionId >> 8) << 3) + chunkX, ((regionId & 255) << 3) + chunkY, z);
        }
    }

    private void loadRegionFromChunk(final int chunkHash) {
        final int chunkX = chunkHash & 2047;
        final int chunkY = (chunkHash >> 11) & 2047;
        final int regionId = (chunkX >> 3) << 8 | (chunkY >> 3);
        cacheRegionAsChunks(regionId);
    }

    private void cacheRegionAsChunks(final int regionId) {
        try {
            final int[] xteas = XTEALoader.getXTEAs(regionId);
            final Cache cache = CacheManager.getCache();
            final Archive archive = cache.getArchive(ArchiveType.MAPS);
            final Group mapGroup = archive.findGroupByName("m" + (regionId >> 8) + "_" + (regionId & 255));
            final Group landGroup = archive.findGroupByName("l" + (regionId >> 8) + "_" + (regionId & 255), xteas);
            final ByteBuffer mapBuffer = mapGroup == null ? null : mapGroup.findFileByID(0).getData();
            final ByteBuffer landBuffer = landGroup == null ? null : landGroup.findFileByID(0).getData();
            if (mapBuffer != null) {
                mapBuffer.setPosition(0);
                splitRegionClipSettingsIntoChunks(regionId, mapBuffer);
            }
            if (landBuffer != null) {
                landBuffer.setPosition(0);
                splitRegionObjectsIntoChunks(regionId, landBuffer);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    private void splitRegionObjectsIntoChunks(final int regionId, final ByteBuffer buffer) {
        final int minChunkX = (regionId >> 8) << 3;
        final int minChunkY = (regionId & 255) << 3;
        int objectId = -1;
        int objectIdDifference;
        while ((objectIdDifference = buffer.readHugeSmart()) != 0) {
            objectId += objectIdDifference;
            int location = 0;
            int locationHashDifference;
            while ((locationHashDifference = buffer.readUnsignedSmart()) != 0) {
                location += locationHashDifference - 1;
                final int hash = buffer.readUnsignedByte();
                final int x = (location >> 6 & 63);
                final int y = (location & 63);
                final int z = location >> 12 & 3;
                final int type = hash >> 2;
                final int rotation = hash & 3;
                final DynamicRegion.InnerChunk innerChunk = getInnerChunk(getChunkHash(minChunkX + (x >> 3),
                        minChunkY + (y >> 3)));
                final DynamicRegion.DynamicObject object = new DynamicObject(objectId, type, rotation);
                final byte localTileHash = getChunkHashInRegion(x - (x >> 3 << 3), y - (y >> 3 << 3), z);
                List<DynamicRegion.DynamicObject> list = innerChunk.objects.get(localTileHash);
                if (list == null) {
                    list = new ArrayList<>();
                    innerChunk.objects.put(localTileHash, list);
                }
                list.add(object);
            }
        }
    }

    private void splitRegionClipSettingsIntoChunks(final int regionId, final ByteBuffer mapBuffer) {
        final int minChunkX = regionId >> 8 << 3;
        final int minChunkY = (regionId & 255) << 3;
        final int increment = mapBuffer == null ? 8 : 1;
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 64; x += increment) {
                for (int y = 0; y < 64; y += increment) {
                    final DynamicRegion.InnerChunk innerChunk = getInnerChunk(getChunkHash(minChunkX + (x >> 3),
                            minChunkY + (y >> 3)));
                    if (mapBuffer == null) {
                        innerChunk.fullyClipped = true;
                        continue;
                    }
                    while (true) {
                        final int value = mapBuffer.readUnsignedShort();
                        if (value == 0) {
                            break;
                        } else if (value == 1) {
                            mapBuffer.readByte();
                            break;
                        } else if (value <= 49) {
                            mapBuffer.readUnsignedShort();
                        } else if (value <= 81) {
                            innerChunk.clipSettings.put(getChunkHashInRegion(x - (x >> 3 << 3), y - (y >> 3 << 3), z)
                                    , (byte) (value - 49));
                        }
                    }
                }
            }
        }
    }

    private InnerChunk getInnerChunk(final int hash) {
        InnerChunk chunk = chunkMap.get(hash);
        if (chunk == null) {
            chunkMap.put(hash, chunk = new InnerChunk());
            return chunk;
        }
        return chunk;
    }

    private int getChunkHash(final int x, final int y) {
        return x | y << 11;
    }

    private byte getChunkHashInRegion(final int x, final int y, final int z) {
        return (byte) (x | y << 3 | z << 6);
    }


    private static final class InnerChunk {
        private final Byte2ByteMap clipSettings;
        private final Byte2ObjectMap<List<DynamicObject>> objects;
        private boolean fullyClipped;

        InnerChunk() {
            clipSettings = new Byte2ByteOpenHashMap(CHUNKS_PER_REGION);
            objects = new Byte2ObjectOpenHashMap<>(CHUNKS_PER_REGION);
        }
    }


    private static final class DynamicObject {
        private final int objectKey;

        DynamicObject(final int id, final int type, final int rotation) {
            objectKey = WorldObject.objectKey(id, type, rotation);
        }

        public int getId() {
            return WorldObject.id(objectKey);
        }

        public int getType() {
            return WorldObject.type(objectKey);
        }

        public int getRotation() {
            return WorldObject.rotation(objectKey);
        }
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static boolean isDynamicRegion(Region region) {
        return region != null && region.isDynamic();
    }

}
