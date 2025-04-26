package com.zenyte.game.world.region;

import com.google.common.base.Preconditions;
import com.zenyte.CacheManager;
import com.zenyte.game.world.object.WorldObject;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.utilities.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public class Regions {
    /**
     * A list of object slots, index is equivalent to type.
     */
    public static final int[] OBJECT_SLOTS = new int[] {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};

    public static final byte[] inject(final byte[] buffer, @Nullable final Predicate<WorldObject> filter, @NotNull final WorldObject... objects) {
        try {
            final ByteBuffer landBuffer = new ByteBuffer(buffer);
            final Collection<WorldObject> mapObjects = MapUtils.decode(landBuffer);
            mapObjects.removeIf(obj -> {
                final int mapObjHash = obj.hashInRegion();
                final int mapObjSlot = OBJECT_SLOTS[obj.getType()];
                for (final WorldObject o : objects) {
                    final int hash = o.hashInRegion();
                    final int slot = OBJECT_SLOTS[o.getType()];
                    if (hash == mapObjHash && slot == mapObjSlot) {
                        return true;
                    }
                }
                return false;
            });
            if (filter != null) {
                mapObjects.removeIf(filter);
            }
            mapObjects.addAll(Arrays.asList(objects));
            final ByteBuffer encoded = MapUtils.encode(mapObjects);
            final ByteBuffer newBuffer = new ByteBuffer(encoded.getBuffer());
            return newBuffer.getBuffer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final byte[] inject(final int regionId, @Nullable final Predicate<WorldObject> filter, @NotNull final WorldObject... objects) {
        try {
            final int[] xteas = XTEALoader.getXTEAs(regionId);
            final Cache cache = CacheManager.getCache();
            final Archive archive = cache.getArchive(ArchiveType.MAPS);
            final Group landGroup = archive.findGroupByName("l" + (regionId >> 8) + "_" + (regionId & 255), xteas);
            final int locFileId = landGroup == null ? -1 : landGroup.getID();
            Preconditions.checkArgument(locFileId != -1);
            final ByteBuffer landBuffer = landGroup == null ? null : landGroup.findFileByID(0).getData();
            final Collection<WorldObject> mapObjects = MapUtils.decode(landBuffer);
            mapObjects.removeIf(obj -> {
                final int mapObjHash = ((obj.getX() & 63) << 6) | (obj.getY() & 63) | ((obj.getPlane() & 3) << 12);
                final int mapObjSlot = OBJECT_SLOTS[obj.getType()];
                for (final WorldObject o : objects) {
                    final int hash = ((o.getX() & 63) << 6) | (o.getY() & 63) | ((o.getPlane() & 3) << 12);
                    final int slot = OBJECT_SLOTS[o.getType()];
                    if (hash == mapObjHash && slot == mapObjSlot) {
                        return true;
                    }
                }
                return false;
            });
            if (filter != null) {
                mapObjects.removeIf(filter);
            }
            mapObjects.addAll(Arrays.asList(objects));
            final ByteBuffer encoded = MapUtils.encode(mapObjects);
            final ByteBuffer newBuffer = new ByteBuffer(encoded.getBuffer());
            return newBuffer.getBuffer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
