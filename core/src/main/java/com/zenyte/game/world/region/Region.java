package com.zenyte.game.world.region;

import com.zenyte.CacheManager;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.packet.out.LocAdd;
import com.zenyte.game.packet.out.LocDel;
import com.zenyte.game.world.SceneSynchronization;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.flooritem.GlobalItem;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.IntArray;
import com.zenyte.utils.efficientarea.Polygon;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.PathIterator;
import java.util.*;
import java.util.function.Predicate;

public class Region {
    public static final int CHUNK_SIZE = 8;

    private static final Logger log = LoggerFactory.getLogger(Region.class);
    /**
     * A volatile integer defining the map load stage. Volatile as it's accessed by multiple threads.
     */
    private volatile int loadStage;
    /**
     * Region's Id.
     */
    protected final int regionId;
    /**
     * A hashmap of the hash of localX, localY, slot & plane, and object.
     */
    protected Short2ObjectMap<WorldObject> objects;
    private final Set<Music> musicTracks = new HashSet<>();
    /**
     * Region's map.
     */
    protected RegionMap map;

    public Region(final int regionId) {
        this.regionId = regionId;
    }

    public final int getId() {
        return regionId;
    }

    private com.zenyte.utils.efficientarea.Area getRepositionedArea(final com.zenyte.utils.efficientarea.Area area,
                                                                    final int xOffset, final int yOffset) {
        final List<int[]> list = new ArrayList<>();
        final PathIterator it = area.getPathIterator(null);
        while (!it.isDone()) {
            final float[] coords = new float[6];
            it.currentSegment(coords);
            it.next();
            if (coords[0] == 0 || coords[1] == 0) {
                continue;
            }
            list.add(IntArray.of((int) coords[0] + xOffset, (int) coords[1] + yOffset));
        }
        final int size = list.size();
        final int[][] array = new int[size][2];
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }
        final RSPolygon rspolygon = new RSPolygon(array);
        final Polygon polygon = rspolygon.getPolygon();
        return new com.zenyte.utils.efficientarea.Area(polygon);
    }

    void removeMultiZone(final int chunkX, final int chunkY, final int z) {
        final Chunk chunk = World.getChunk(Chunk.getChunkHash(chunkX, chunkY, z));
        final List<Chunk.RSArea> multiAreas = chunk.getMultiPolygons();
        if (multiAreas != null) {
            for (int i = multiAreas.size() - 1; i >= 0; i--) {
                final Chunk.RSArea area = multiAreas.get(i);
                //MultiwayArea.removeDynamicMultiArea(area);
            }
        }
    }

    public final RegionMap getRegionMap(final boolean load) {
        if (load && map == null) {
            map = new RegionMap(regionId);
        }
        return map;
    }

    public final int getMask(final int plane, final int localX, final int localY) {
        if (map == null || loadStage != 2) {
            return -1;
        }
        return map.getLocalMask(plane, localX & 63, localY & 63);
    }

    public final int getLoadStage() {
        return loadStage;
    }

    public final void setLoadStage(final int loadMapStage) {
        loadStage = loadMapStage;
    }

    /**
     * Get's ground item with specific id on the specific location in this region.
     */
    public final FloorItem getFloorItem(final int id, final Location tile, final Player player) {
        final Chunk chunk = this.getChunk(tile.getX() & 63, tile.getY() & 63, tile.getPlane());
        final Set<FloorItem> floorItems = chunk.getFloorItems();
        if (floorItems == null) {
            return null;
        }
        FloorItem unpickableItem = null;
        for (final FloorItem item : floorItems) {
            if (!item.isVisibleTo(player)) {
                continue;
            }
            if (item.getId() == id && tile.getPositionHash() == item.getLocation().getPositionHash()) {
                if (player.isIronman() && !player.getUsername().equals(item.getOwnerName())) {
                    if (unpickableItem == null) {
                        unpickableItem = item;
                        continue;
                    }
                }
                return item;
            }
        }
        return unpickableItem;
    }

    /**
     * Get's ground item with specific id on the specific location in this region.
     */
    public final FloorItem getFloorItem(final int id, final Location tile) {
        final Chunk chunk = this.getChunk(tile.getX() & 63, tile.getY() & 63, tile.getPlane());
        final Set<FloorItem> floorItems = chunk.getFloorItems();
        if (floorItems == null) {
            return null;
        }
        for (final FloorItem item : floorItems) {
            if (item.getId() == id && tile.getPositionHash() == item.getLocation().getPositionHash()) {
                return item;
            }
        }
        return null;
    }

    public final void setMask(final int plane, final int localX, final int localY, final int mask) {
        if (map == null || loadStage != 2) {
            return;
        }
        if (localX >= 64 || localY >= 64 || localX < 0 || localY < 0) {
            final Location tile = new Location(map.getRegionX() + localX, map.getRegionY() + localY, plane);
            final int regionId = tile.getRegionId();
            final int newRegionX = (regionId >> 8) * 64;
            final int newRegionY = (regionId & 255) * 64;
            World.getRegion(tile.getRegionId(), false).setMask(plane, tile.getX() - newRegionX,
                    tile.getY() - newRegionY, mask);
            return;
        }
        map.setFlag(plane, localX, localY, mask);
    }

    public final void addFlag(final int plane, final int localX, final int localY, final int mask) {
        if (map == null || loadStage != 2) {
            return;
        }
        map.setFlag(plane, localX, localY, mask, true);
    }

    public final void removeFlag(final int plane, final int localX, final int localY, final int mask) {
        if (map == null || loadStage != 2) {
            return;
        }
        map.setFlag(plane, localX, localY, mask, false);
    }

    public final void unclip(final int plane, final int x, final int y) {
        if (map == null) {
            map = new RegionMap(regionId);
        }
        map.setFlag(plane, x, y, 0);
    }

    public void destroy() {
        loadStage = 0;
        if (objects != null) {
            objects.clear();
            objects = null;
        }
        musicTracks.clear();
        map = null;
    }

    public void load() {
        if (loadStage != 0) {
            return;
        }
        loadStage = 1;
        try {
            final Set<GlobalItem> set = GlobalItem.getGlobalItems(regionId);
            if (set != null) {
                for (final GlobalItem item : set) {
                    item.spawn();
                }
            }
            loadRegionMap();
            loadStage = 2;
        } catch (final Throwable e) {
            log.error("", e);
        }
    }

    public final void clip(final int plane, final int x, final int y) {
        if (map == null) {
            map = new RegionMap(regionId);
        }
        map.setFlag(plane, x, y, Flags.FLOOR_DECORATION);
    }

    private final void clip(final WorldObject object, final int x, final int y) {
        if (map == null) {
            map = new RegionMap(regionId);
        }
        final int plane = object.getPlane();
        final int type = object.getType();
        final int orientation = object.getRotation();
        if (x < 0 || y < 0 || x >= 64 || y >= 64) {
            return;
        }
        final ObjectDefinitions objectDefinition = ObjectDefinitions.get(object.getId());
        if (objectDefinition == null) {
            return;
        }
        if (type == 22 ? objectDefinition.getClipType() != 1 : objectDefinition.getClipType() == 0) {
            return;
        }
        if (type >= 0 && type <= 3) {
            map.setWall(plane, x, y, type, orientation, objectDefinition.isProjectileClip(), true);
        } else if (type >= 9 && type <= 21) {
            final boolean reverseSizes = (orientation & 1) == 1;
            final int width = reverseSizes ? objectDefinition.getSizeY() : objectDefinition.getSizeX();
            final int height = reverseSizes ? objectDefinition.getSizeX() : objectDefinition.getSizeY();
            map.setObject(plane, x, y, width, height, objectDefinition.isProjectileClip(), true);
        } else if (type == 22) {
            map.setFloor(plane, x, y, true);
        }
    }

    private final void unclip(final WorldObject object, final int x, final int y) {
        if (map == null) {
            map = new RegionMap(regionId);
        }
        final int plane = object.getPlane();
        final int type = object.getType();
        final int rotation = object.getRotation();
        if (x < 0 || y < 0 || x >= 64 || y >= 64) {
            return;
        }
        final ObjectDefinitions objectDefinition = ObjectDefinitions.get(object.getId());
        if (objectDefinition == null) {
            return;
        }
        if (type == 22 ? objectDefinition.getClipType() != 1 : objectDefinition.getClipType() == 0) {
            return;
        }
        if (type >= 0 && type <= 3) {
            map.setWall(plane, x, y, type, rotation, objectDefinition.isProjectileClip(), false);
        } else if (type >= 9 && type <= 21) {
            final boolean reverseSizes = (rotation & 1) == 1;
            final int width = reverseSizes ? objectDefinition.getSizeY() : objectDefinition.getSizeX();
            final int height = reverseSizes ? objectDefinition.getSizeX() : objectDefinition.getSizeY();
            map.setObject(plane, x, y, width, height, objectDefinition.isProjectileClip(), false);
        } else if (type == 22) {
            map.setFloor(plane, x, y, false);
        }
    }

    private Chunk getChunk(final int localX, final int localY, final int plane) {
        final int x = ((regionId >> 8 & 255) << 6) + localX;
        final int y = ((regionId & 255) << 6) + localY;
        final int chunkId = Chunk.getChunkHash(x >> 3, y >> 3, plane);
        return World.getChunk(chunkId);
    }

    public void spawnObject(final WorldObject object, final int plane, final int localX, final int localY,
                            final boolean mapObject, final boolean alterClipping) {
        if (objects == null) {
            objects = new Short2ObjectOpenHashMap<>();
        }
        final int slot = Regions.OBJECT_SLOTS[object.getType()];
        final short hash = (short) (localX | localY << 6 | slot << 12 | plane << 14);
        if (mapObject) {
            objects.put(hash, object);
            if (alterClipping) {
                clip(object, localX, localY);
            }
            return;
        }
        final Chunk chunk = this.getChunk(localX, localY, plane);
        final Short2ObjectMap<WorldObject> originalObjects = chunk.getOriginalObjects();
        final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
        originalObjects.remove(hash);
        final WorldObject original = objects.get(hash);
        final WorldObject spawned = spawnedObjects.remove(hash);
        if (spawned != null && alterClipping) {
            unclip(spawned, localX, localY);
        }
        if (!Objects.equals(original, object)) {
            if (original != null && alterClipping) {
                unclip(original, localX, localY);
            }
            spawnedObjects.put(hash, object);
            originalObjects.put(hash, object);
        }
        if (alterClipping) {
            clip(object, localX, localY);
        }
        SceneSynchronization.forEach(object, player -> new LocAdd(object));
    }

    public void removeObject(final WorldObject object, final int plane, final int localX, final int localY) {
        if (objects == null) {
            objects = new Short2ObjectOpenHashMap<>();
        }
        final int slot = Regions.OBJECT_SLOTS[object.getType()];
        final short hash = (short) (localX | localY << 6 | slot << 12 | plane << 14);
        final Chunk chunk = this.getChunk(localX, localY, plane);
        final Short2ObjectMap<WorldObject> originalObjects = chunk.getOriginalObjects();
        final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
        final WorldObject spawned = spawnedObjects.remove(hash);
        final WorldObject current = objects.get(hash);
        if (spawned != null) {
            unclip(spawned, localX, localY);
        }
        if (current != null) {
            unclip(current, localX, localY);
            originalObjects.put(hash, current);
        }
        SceneSynchronization.forEach(object, player -> new LocDel(object));
    }

    private final WorldObject getSpawnedObjectWithSlot(final int plane, final int x, final int y, final int slot) {
        final Chunk chunk = this.getChunk(x, y, plane);
        final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
        if (spawnedObjects == null) {
            return null;
        }
        final short hash = (short) (x | y << 6 | slot << 12 | plane << 14);
        return spawnedObjects.get(hash);
    }

    public void loadRegionMap() {
        try {
            final int baseX = (regionId >> 8) * 64;
            final int baseY = (regionId & 255) * 64;
            final int[] xteas = XTEALoader.getXTEAs(regionId);
            final Cache cache = CacheManager.getCache();
            final Archive archive = cache.getArchive(ArchiveType.MAPS);
            final Group mapGroup = archive.findGroupByName("m" + (regionId >> 8) + "_" + (regionId & 255));
            final Group landGroup = archive.findGroupByName("l" + (regionId >> 8) + "_" + (regionId & 255), xteas);
            final ByteBuffer mapBuffer = mapGroup == null ? null : mapGroup.findFileByID(0).getData();
            final ByteBuffer landBuffer = landGroup == null ? null : landGroup.findFileByID(0).getData();
            final byte[][][] mapSettings = mapBuffer == null ? null : new byte[4][64][64];
            if (mapBuffer != null) {
                mapBuffer.setPosition(0);
                for (int plane = 0; plane < 4; plane++) {
                    for (int x = 0; x < 64; x++) {
                        for (int y = 0; y < 64; y++) {
                            while (true) {
                                final int value = mapBuffer.readUnsignedShort();
                                if (value == 0) {
                                    break;
                                } else if (value == 1) {
                                    mapBuffer.readByte();
                                    break;
                                } else if (value <= 49) {
                                    mapBuffer.readShort();
                                } else if (value <= 81) {
                                    mapSettings[plane][x][y] = (byte) (value - 49);
                                }
                            }
                        }
                    }
                }
                for (int plane = 0; plane < 4; plane++) {
                    for (int x = 0; x < 64; x++) {
                        for (int y = 0; y < 64; y++) {
                            if ((mapSettings[plane][x][y] & 1) == 1) {
                                int realPlane = plane;
                                if ((mapSettings[1][x][y] & 2) == 2) {
                                    realPlane--;
                                }
                                if (realPlane >= 0) {
                                    getRegionMap(true).setFloor(realPlane, x, y, true);
                                }
                            }
                        }
                    }
                }
            } else {
                for (int plane = 0; plane < 4; plane++) {
                    for (int x = 0; x < 64; x++) {
                        for (int y = 0; y < 64; y++) {
                            getRegionMap(true).setFloor(plane, x, y, true);
                        }
                    }
                }
            }
            if (landBuffer != null) {
                landBuffer.setPosition(0);
                int objectId = -1;
                int incr;
                while ((incr = landBuffer.readHugeSmart()) != 0) {
                    objectId += incr;
                    int location = 0;
                    int incr2;
                    while ((incr2 = landBuffer.readUnsignedSmart()) != 0) {
                        location += incr2 - 1;
                        final int localX = (location >> 6 & 63);
                        final int localY = (location & 63);
                        final int plane = location >> 12;
                        final int objectData = landBuffer.readUnsignedByte();
                        final int type = objectData >> 2;
                        final int rotation = objectData & 3;
                        int objectPlane = plane;
                        if (mapSettings != null && (mapSettings[1][localX][localY] & 2) == 2) {
                            objectPlane--;
                        }
                        if (objectPlane < 0 || objectPlane >= 4 || plane < 0 || plane >= 4) {
                            continue;
                        }
                        spawnObject(new WorldObject(objectId, type, rotation,
                                localX + baseX, localY + baseY, objectPlane), objectPlane, localX, localY, true, true);
                    }
                }
            }
            //log.info("Loaded region {}", regionId);
        } catch (final Exception e) {
            if (log.isErrorEnabled())
                log.error("Failed to load region {}", regionId);
        }
    }

    public boolean containsSpawnedObject(final WorldObject object) {
        final Chunk chunk = this.getChunk(object.getX() & 63, object.getY() & 63, object.getPlane());
        final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
        if (spawnedObjects == null) {
            return false;
        }
        final short hash =
                (short) (object.getXInRegion() | object.getYInRegion() << 6 | Regions.OBJECT_SLOTS[object.getType()] << 12 | object.getPlane() << 14);
        final WorldObject obj = spawnedObjects.get(hash);
        return obj == object;
    }

    public boolean containsObjectWithId(final int plane, final int x, final int y, final int id) {
        final WorldObject object = getObjectWithId(plane, x, y, id);
        return object != null && object.getId() == id;
    }

    public WorldObject getObjectWithId(final int plane, final int x, final int y, final int id) {
        if (objects == null) {
            return null;
        }
        for (int i = 0; i < 4; i++) {
            final short hash = (short) (x | y << 6 | i << 12 | plane << 14);
            final Chunk chunk = this.getChunk(x, y, plane);
            final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
            final WorldObject object = spawnedObjects.get(hash);
            if (object != null) {
                if (object.getId() == id) {
                    return object;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            final short hash = (short) (x | y << 6 | i << 12 | plane << 14);
            final WorldObject object = objects.get(hash);
            if (object == null) continue;
            if (object.getId() == id) {
                final Chunk chunk = this.getChunk(x, y, plane);
                final Short2ObjectMap<WorldObject> removedObjects = chunk.getOriginalObjects();
                if (removedObjects.get(hash) != null) continue;
                return object;
            }
        }
        return null;
    }

    public boolean containsObjectWithEqualSlot(final int plane, final int x, final int y, final int type) {
        return getObjectOfSlot(plane, x, y, type) != null;
    }

    public WorldObject getObjectWithType(final Location tile, final int type) {
        return getObjectWithType(tile.getPlane(), tile.getX(), tile.getY(), type);
    }

    public WorldObject getObjectOfSlot(final int locationZ, final int locationX, final int locationY, final int type) {
        if (objects == null) {
            return null;
        }
        final int x = locationX & 63;
        final int y = locationY & 63;
        final int plane = locationZ & 3;
        final int slot = Regions.OBJECT_SLOTS[type];
        final short hash = (short) (x | y << 6 | slot << 12 | plane << 14);
        final Chunk chunk = this.getChunk(x, y, plane);
        final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
        final WorldObject spawnedObj = spawnedObjects.get(hash);
        if (spawnedObj != null) {
            return spawnedObj;
        }
        final WorldObject object = objects.get(hash);
        if (object != null) {
            final WorldObject spawned = getSpawnedObjectWithSlot(plane, x, y, Regions.OBJECT_SLOTS[object.getType()]);
            if (spawned == null) {
                final Short2ObjectMap<WorldObject> originalObjects = chunk.getOriginalObjects();
                if (originalObjects.containsKey(hash)) {
                    return null;
                }
            }
            return spawned == null ? object : null;
        }
        return null;
    }

    public WorldObject getObjectWithType(final int locationZ, final int locationX, final int locationY,
                                         final int type) {
        if (objects == null) {
            return null;
        }
        final int x = locationX & 63;
        final int y = locationY & 63;
        final int plane = locationZ & 3;
        final int slot = Regions.OBJECT_SLOTS[type];
        final short hash = (short) (x | y << 6 | slot << 12 | plane << 14);
        final Chunk chunk = this.getChunk(x, y, plane);
        final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
        final WorldObject spawnedObj = spawnedObjects.get(hash);
        if (spawnedObj != null) {
            return spawnedObj;
        }
        final WorldObject object = objects.get(hash);
        if (object != null && object.getType() == type) {
            final WorldObject spawned = getSpawnedObjectWithSlot(plane, x, y, Regions.OBJECT_SLOTS[object.getType()]);
            if (spawned == null) {
                final Short2ObjectMap<WorldObject> originalObjects = chunk.getOriginalObjects();
                if (originalObjects.containsKey(hash)) {
                    return null;
                }
            }
            return spawned == null ? object : null;
        }
        return null;
    }

    public boolean containsObject(final int id, final int type, final Location tile) {
        final int absX = (regionId >> 8) * 64;
        final int absY = (regionId & 255) * 64;
        final int localX = tile.getX() - absX;
        final int localY = tile.getY() - absY;
        if (localX < 0 || localY < 0 || localX >= 64 || localY >= 64) {
            return false;
        }
        final WorldObject spawnedObject = getSpawnedObject(tile,
                object -> Regions.OBJECT_SLOTS[object.getType()] == Regions.OBJECT_SLOTS[type]);
        if (spawnedObject != null) {
            return spawnedObject.getId() == id;
        }
        final WorldObject[] mapObjects = getObjects(tile.getPlane(), localX, localY);
        if (mapObjects == null || getRemovedObject(tile,
                object -> Regions.OBJECT_SLOTS[object.getType()] == Regions.OBJECT_SLOTS[type]) != null) {
            return false;
        }
        for (final WorldObject object : mapObjects) {
            if (object == null) {
                continue;
            }
            if (object.getId() == id && object.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public WorldObject[] getObjects(final int plane, final int x, final int y) {
        if (objects == null) {
            return null;
        }
        final WorldObject[] objs = new WorldObject[4];
        for (int i = 0; i < 4; i++) {
            final short hash = (short) (x | y << 6 | i << 12 | plane << 14);
            objs[i] = objects.get(hash);
        }
        return objs;
    }

    public WorldObject getSpawnedObject(final Location tile, final Predicate<WorldObject> predicate) {
        final Chunk chunk = this.getChunk(tile.getX() & 63, tile.getY() & 63, tile.getPlane());
        final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
        if (spawnedObjects == null) {
            return null;
        }
        for (int i = 0; i < 4; i++) {
            final short hash =
                    (short) (tile.getXInRegion() | tile.getYInRegion() << 6 | i << 12 | tile.getPlane() << 14);
            final WorldObject obj = spawnedObjects.get(hash);
            if (obj != null && predicate.test(obj)) {
                return obj;
            }
        }
        return null;
    }

    public WorldObject getRemovedObject(final Location tile, final Predicate<WorldObject> predicate) {
        final Chunk chunk = this.getChunk(tile.getX() & 63, tile.getY() & 63, tile.getPlane());
        final Short2ObjectMap<WorldObject> removedObjects = chunk.getOriginalObjects();
        if (removedObjects == null) {
            return null;
        }
        for (int i = 0; i < 4; i++) {
            final short hash =
                    (short) (tile.getXInRegion() | tile.getYInRegion() << 6 | i << 12 | tile.getPlane() << 14);
            final WorldObject obj = removedObjects.get(hash);
            if (obj != null && predicate.test(obj)) {
                return obj;
            }
        }
        return null;
    }

    public WorldObject getSpawnedObject(final Location tile) {
        final Chunk chunk = this.getChunk(tile.getX() & 63, tile.getY() & 63, tile.getPlane());
        final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
        if (spawnedObjects == null) {
            return null;
        }
        for (int i = 0; i < 4; i++) {
            final short hash =
                    (short) (tile.getXInRegion() | tile.getYInRegion() << 6 | i << 12 | tile.getPlane() << 14);
            final WorldObject obj = spawnedObjects.get(hash);
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    public Short2ObjectMap<WorldObject> getObjects() {
        return objects;
    }

    public Set<Music> getMusicTracks() {
        return musicTracks;
    }

    public boolean isDynamic() {
        return false;
    }

}
