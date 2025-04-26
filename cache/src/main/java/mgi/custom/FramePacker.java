package mgi.custom;

import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.utilities.ByteBuffer;

/**
 * @author Kris | 28/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FramePacker {
    ;

    private static final Int2ObjectMap<byte[]> frames = new Int2ObjectOpenHashMap<>();

    public static void reset() {
        frames.clear();
    }

    public static void add(final int file, final byte[] frames) {
        FramePacker.frames.put(file, frames);
    }

    public static void write() {
        final Cache cache = CacheManager.getCache();
        final Archive archive = cache.getArchive(ArchiveType.FRAMES);
        final Int2ObjectAVLTreeMap<byte[]> sortedMap = new Int2ObjectAVLTreeMap<>(frames);
        for (final Int2ObjectMap.Entry<byte[]> entry : sortedMap.int2ObjectEntrySet()) {
            final int key = entry.getIntKey();
            final byte[] bytes = entry.getValue();
            final File file = new File(key & 65535, new ByteBuffer(bytes));
            final Group group = archive.findGroupByID(key >> 16);
            if (group == null) {
                archive.addGroup(new Group(key >> 16, file));
            } else {
                group.addFile(file);
            }
        }
    }

}
