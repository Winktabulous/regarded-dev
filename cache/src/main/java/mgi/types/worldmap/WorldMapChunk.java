package mgi.types.worldmap;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.utilities.ByteBuffer;

/**
 * @author Tommeh | 4-12-2018 | 19:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WorldMapChunk extends WorldMapNode {
    public int chunkX;
    public int chunkY;
    public int field332;
    public int field328;
    public int check1;
    public int check2;
    public int expectedRegionX;
    public int expectedRegionY;
    public int expectedChunkX;
    public int expectedChunkY;
    public int a;
    public int b;

    public void decode(final ByteBuffer compositeMapBuffer) {
        check1 = compositeMapBuffer.readUnsignedByte();
        super.minPlane = compositeMapBuffer.readUnsignedByte();
        super.maxPlane = compositeMapBuffer.readUnsignedByte();
        super.centerRegionX = compositeMapBuffer.readUnsignedShort();
        super.centerRegionY = compositeMapBuffer.readUnsignedShort();
        this.field332 = compositeMapBuffer.readUnsignedByte();
        this.field328 = compositeMapBuffer.readUnsignedByte();
        super.regionX = compositeMapBuffer.readUnsignedShort();
        super.regionY = compositeMapBuffer.readUnsignedShort();
        this.chunkX = compositeMapBuffer.readUnsignedByte();
        this.chunkY = compositeMapBuffer.readUnsignedByte();
        super.maxPlane = Math.min(super.maxPlane, 4);
        super.underlays = new short[1][64][64];
        super.overlays = new short[super.maxPlane][64][64];
        super.overlayShapes = new byte[super.maxPlane][64][64];
        super.overlayRotations = new byte[super.maxPlane][64][64];
        super.objects = new WorldMapGameObject[super.maxPlane][64][64][];
        a = compositeMapBuffer.readBigSmart();
        b = compositeMapBuffer.readBigSmart();
        final Archive archive = CacheManager.getCache().getArchive(18);
        final Group group = archive.findGroupByID(a);
        final File file = group.findFileByID(b);
        final ByteBuffer areaBuffer = file.getData();
        check2 = areaBuffer.readUnsignedByte();
        expectedRegionX = areaBuffer.readUnsignedByte();
        expectedRegionY = areaBuffer.readUnsignedByte();
        expectedChunkX = areaBuffer.readUnsignedByte();
        expectedChunkY = areaBuffer.readUnsignedByte();
        if (expectedRegionX == super.regionX && expectedRegionY == super.regionY && expectedChunkX == this.chunkX && expectedChunkY == this.chunkY) {
            for (int chunkXOffset = 0; chunkXOffset < 8; ++chunkXOffset) {
                for (int chunkYOffset = 0; chunkYOffset < 8; ++chunkYOffset) {
                    this.decodeTile(chunkXOffset + this.chunkX * 8, chunkYOffset + this.chunkY * 8, areaBuffer);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "WorldMapChunk(super=" + super.toString() + ", chunkX=" + this.chunkX + ", chunkY=" + this.chunkY + ", field332=" + this.field332 + ", field328=" + this.field328 + ", check1=" + this.check1 + ", check2=" + this.check2 + ", expectedRegionX=" + this.expectedRegionX + ", expectedRegionY=" + this.expectedRegionY + ", expectedChunkX=" + this.expectedChunkX + ", expectedChunkY=" + this.expectedChunkY + ", a=" + this.a + ", b=" + this.b + ")";
    }
}
