package com.zenyte.game.util;

import com.zenyte.CacheManager;
import com.zenyte.game.world.entity._Location;
import com.zenyte.game.world.region.XTEALoader;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.utilities.ByteBuffer;

import java.io.File;
import java.util.Arrays;

/**
 * @author Kris | 24/10/2018 12:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MapPacker {

    public static final void main(final String... args) throws Throwable {
        new MapPacker().repack();
    }

    private void repack() throws Throwable {
        CacheManager.loadDetached();
        XTEALoader.load("cache/data/objects/xteas.json");
        final Cache cache = CacheManager.getCache();
        final Archive archive = cache.getArchive(ArchiveType.MAPS);
        for (int x = 0; x < 16383; x += 64) {
            for (int y = 0; y < 16383; y += 64) {
                final int regionId = _Location.getRegionId(x, y);
                Group group = null;
                try {
                    final int[] xteas = XTEALoader.getXTEAKeys(regionId);
                    group = archive.findGroupByName("l" + (regionId >> 8) + "_" + (regionId & 255), xteas);
                    if (group == null) {
                        continue;
                    }
                    System.out.println("Writing region: " + regionId+", "+ Arrays.toString(xteas));
                    group.setXTEA(new int[4]);
                    group.finish();
                } catch (RuntimeException e) {
                    group = archive.findGroupByName("l" + (regionId >> 8) + "_" + (regionId & 255), null, false);
                    if (group != null) {
                        archive.deleteGroup(group);
                    }

                    final Group newLandGroup = new Group(archive.getFreeGroupID(),
                            new mgi.tools.jagcached.cache.File(0, new ByteBuffer(new byte[] {0})));
                    newLandGroup.setName("l" + (regionId >> 8) + "_" + (regionId & 255));
                    newLandGroup.setXTEA(new int[4]);
                    archive.addGroup(newLandGroup);
                    //e.printStackTrace();
                    /*if (group == null) {
                        continue;
                    }
                    System.err.println("rewriting region: " + regionId);
                    group.findFileByID(0).setData(new ByteBuffer(new byte[] {0}));
                    group.setXTEA(new int[4]);*/
                }
            }
        }
        CacheManager.getCache().close();
    }
}
