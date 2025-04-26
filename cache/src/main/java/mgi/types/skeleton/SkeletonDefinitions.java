package mgi.types.skeleton;

import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;

/**
 * @author Kris | 19. sept 2018 : 16:57:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SkeletonDefinitions implements Definitions {

    private static final Int2ObjectOpenHashMap<SkeletonDefinitions> definitions =
            new Int2ObjectOpenHashMap<SkeletonDefinitions>(1000);

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        final Archive skeletons = cache.getArchive(ArchiveType.FRAMES);
        for (int i = 0; i < skeletons.getHighestGroupId(); i++) {
            final Group skeletonGroup = skeletons.findGroupByID(i);
            if (skeletonGroup == null) continue;
            for (int id = 0; id < skeletonGroup.getHighestFileId(); id++) {
                final File file = skeletonGroup.findFileByID(id);
                if (file == null) continue;
                final ByteBuffer buffer = file.getData();
                if (buffer == null) continue;
                definitions.put(i << 16 | id, new SkeletonDefinitions(i << 16 | id, buffer));
            }
        }
    }

    public static IntOpenHashSet getLinkedFrames(final int frameMapId) {
        final IntOpenHashSet set = new IntOpenHashSet();
        final ObjectIterator<Int2ObjectMap.Entry<SkeletonDefinitions>> iterator =
                SkeletonDefinitions.getDefinitions().int2ObjectEntrySet().fastIterator();
        while (iterator.hasNext()) {
            final Int2ObjectMap.Entry<SkeletonDefinitions> next = iterator.next();
            final SkeletonDefinitions definitions = next.getValue();
            if (definitions.frameMapId == frameMapId) {
                set.add(definitions.frameId);
            }
        }
        return set;
    }

    private SkeletonDefinitions(final int frameId, final ByteBuffer buffer) {
        this.frameId = frameId;
        decode(buffer);
    }

    private final int frameId;
    private int frameMapId;

    public static SkeletonDefinitions get(final int frameId) {
        return definitions.get(frameId);
    }

    @Override
    public void decode(final ByteBuffer buffer) {
        if (buffer.remaining() >= 2) {
            frameMapId = buffer.readUnsignedShort();
        }
    }

    public SkeletonDefinitions() {
        this.frameId = 0;
    }

    public static Int2ObjectOpenHashMap<SkeletonDefinitions> getDefinitions() {
        return definitions;
    }

    public int getFrameId() {
        return frameId;
    }

    public int getFrameMapId() {
        return frameMapId;
    }

}
