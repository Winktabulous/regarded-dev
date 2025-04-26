package mgi.types.config;

import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Kris | 6. apr 2018 : 21:12.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 * profile</a>}
 */
public final class SpotAnimationDefinition implements Definitions {
    private static final Logger log = LoggerFactory.getLogger(SpotAnimationDefinition.class);
    public static Int2ObjectOpenHashMap<SpotAnimationDefinition> definitions = new Int2ObjectOpenHashMap<>();
    private int id;
    private int modelId;
    private int animationId;
    private int widthScale;
    private int heightScale;
    private int orientation;
    private int ambient;
    private int contrast;
    short[] recolorFrom;
    short[] retextureFrom;
    short[] recolorTo;
    short[] retextureTo;

    public SpotAnimationDefinition(final int id) {
        this.id = id;
        setDefaults();
    }

    public SpotAnimationDefinition(final int id, final ByteBuffer buffer) {
        this.id = id;
        setDefaults();
        decode(buffer);
    }

    public static SpotAnimationDefinition get(final int id) {
        return definitions.getOrDefault(id, null);
    }

    public static final void printGraphicsDifferences(final Cache cache, final Cache cacheToCompareWith) {
        final Int2ObjectOpenHashMap<byte[]> currentAnimations = getAnimations(cache);
        final Int2ObjectOpenHashMap<byte[]> animations = getAnimations(cacheToCompareWith);
        ObjectIterator<Int2ObjectMap.Entry<byte[]>> iterator = currentAnimations.int2ObjectEntrySet().iterator();
        final IntArrayList list = new IntArrayList();
        while (iterator.hasNext()) {
            final Int2ObjectMap.Entry<byte[]> next = iterator.next();
            final int id = next.getIntKey();
            final byte[] bytes = next.getValue();
            final byte[] otherBytes = animations.get(id);
            if (!Arrays.equals(bytes, otherBytes)) {
                list.add(id);
            }
        }
        iterator = animations.int2ObjectEntrySet().iterator();
        while (iterator.hasNext()) {
            final Int2ObjectMap.Entry<byte[]> next = iterator.next();
            final int id = next.getIntKey();
            final byte[] bytes = next.getValue();
            final byte[] otherBytes = currentAnimations.get(id);
            if (otherBytes == null || !Arrays.equals(bytes, otherBytes)) {
                if (!list.contains(id)) list.add(id);
            }
        }
        Collections.sort(list);
        for (int id : list) {
            System.err.println("Graphics difference: " + id);
        }
        System.err.println("Graphics difference checking complete!");
    }

    private static final Int2ObjectOpenHashMap<byte[]> getAnimations(final Cache cache) {
        final Int2ObjectOpenHashMap<byte[]> map = new Int2ObjectOpenHashMap<byte[]>();
        try {
            final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
            final Group graphics = configs.findGroupByID(GroupType.SPOTANIM);
            for (int id = 0; id < graphics.getHighestFileId(); id++) {
                final File file = graphics.findFileByID(id);
                if (file == null) {
                    continue;
                }
                final ByteBuffer buffer = file.getData();
                if (buffer == null) {
                    continue;
                }
                map.put(id, buffer.getBuffer());
            }
        } catch (final Exception e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group graphics = configs.findGroupByID(GroupType.SPOTANIM);
        for (int id = 0; id < graphics.getHighestFileId(); id++) {
            final File file = graphics.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            definitions.put(id, new SpotAnimationDefinition(id, buffer));
        }
    }

    private void setDefaults() {
        animationId = -1;
        widthScale = 128;
        heightScale = 128;
        orientation = 0;
        ambient = 0;
        contrast = 0;
    }

    @Override
    public void decode(final ByteBuffer buffer) {
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }
            decode(buffer, opcode);
        }
    }

    @Override
    public void decode(final ByteBuffer buffer, final int opcode) {
        switch (opcode) {
            case 1 -> modelId = buffer.readUnsignedShort();
            case 2 -> animationId = buffer.readUnsignedShort();
            case 4 -> widthScale = buffer.readUnsignedShort();
            case 5 -> heightScale = buffer.readUnsignedShort();
            case 6 -> orientation = buffer.readUnsignedShort();
            case 7 -> ambient = buffer.readUnsignedByte();
            case 8 -> contrast = buffer.readUnsignedByte();
            case 40 -> {
                final int length = buffer.readUnsignedByte();
                recolorFrom = new short[length];
                recolorTo = new short[length];
                for (int index = 0; index < length; ++index) {
                    recolorFrom[index] = (short) buffer.readUnsignedShort();
                    recolorTo[index] = (short) buffer.readUnsignedShort();
                }
            }
            case 41 -> {
                {
                    final int length = buffer.readUnsignedByte();
                    retextureFrom = new short[length];
                    retextureTo = new short[length];
                    for (int index = 0; index < length; ++index) {
                        retextureFrom[index] = (short) buffer.readUnsignedShort();
                        retextureTo[index] = (short) buffer.readUnsignedShort();
                    }
                }
            }
        }
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(128);
        if (modelId != -1) {
            buffer.writeUnsignedByte(1);
            buffer.writeUnsignedShort(modelId);
        }
        if (animationId != -1) {
            buffer.writeUnsignedByte(2);
            buffer.writeUnsignedShort(animationId);
        }
        if (widthScale != 0) {
            buffer.writeUnsignedByte(4);
            buffer.writeUnsignedShort(widthScale);
        }
        if (heightScale != 0) {
            buffer.writeUnsignedByte(5);
            buffer.writeUnsignedShort(heightScale);
        }
        if (orientation != -1) {
            buffer.writeUnsignedByte(6);
            buffer.writeUnsignedShort(orientation);
        }
        if (ambient != -1) {
            buffer.writeUnsignedByte(7);
            buffer.writeUnsignedByte(ambient);
        }
        if (contrast != -1) {
            buffer.writeUnsignedByte(8);
            buffer.writeUnsignedByte(contrast);
        }
        if (recolorFrom != null && recolorFrom.length > 0) {
            buffer.writeUnsignedByte(40);
            buffer.writeUnsignedByte(recolorFrom.length);
            for (int index = 0; index < recolorFrom.length; index++) {
                buffer.writeUnsignedShort(recolorFrom[index]);
                buffer.writeUnsignedShort(recolorTo[index]);
            }
        }
        if (retextureFrom != null && retextureFrom.length > 0) {
            buffer.writeUnsignedByte(41);
            buffer.writeUnsignedByte(retextureFrom.length);
            for (int index = 0; index < retextureFrom.length; index++) {
                buffer.writeUnsignedShort(retextureFrom[index]);
                buffer.writeUnsignedShort(retextureTo[index]);
            }
        }
        buffer.writeUnsignedByte(0);
        return buffer;
    }

    @Override
    public void pack() {
        pack(id, encode());
    }

    public static void pack(int id, ByteBuffer buffer) {
        CacheManager.getCache()
                .getArchive(ArchiveType.CONFIGS)
                .findGroupByID(GroupType.SPOTANIM)
                .addFile(new File(id, buffer));
    }

    public SpotAnimationDefinition() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getAnimationId() {
        return animationId;
    }

    public void setAnimationId(int animationId) {
        this.animationId = animationId;
    }

    public int getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(int widthScale) {
        this.widthScale = widthScale;
    }

    public int getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(int heightScale) {
        this.heightScale = heightScale;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getAmbient() {
        return ambient;
    }

    public void setAmbient(int ambient) {
        this.ambient = ambient;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public short[] getRecolorFrom() {
        return recolorFrom;
    }

    public void setRecolorFrom(short[] recolorFrom) {
        this.recolorFrom = recolorFrom;
    }

    public short[] getRetextureFrom() {
        return retextureFrom;
    }

    public void setRetextureFrom(short[] retextureFrom) {
        this.retextureFrom = retextureFrom;
    }

    public short[] getRecolorTo() {
        return recolorTo;
    }

    public void setRecolorTo(short[] recolorTo) {
        this.recolorTo = recolorTo;
    }

    public short[] getRetextureTo() {
        return retextureTo;
    }

    public void setRetextureTo(short[] retextureTo) {
        this.retextureTo = retextureTo;
    }

    public SpotAnimationDefinition copy(int newId) {
        final SpotAnimationDefinition copy = new SpotAnimationDefinition(newId);
        copy.setModelId(modelId);
        copy.setWidthScale(widthScale);
        copy.setHeightScale(heightScale);
        copy.setOrientation(orientation);
        copy.setAmbient(ambient);
        copy.setContrast(contrast);
        copy.setAnimationId(animationId);
        if (recolorFrom != null)
            copy.setRecolorFrom(Arrays.copyOf(recolorFrom, recolorFrom.length));
        if (recolorTo != null)
            copy.setRecolorTo(Arrays.copyOf(recolorTo, recolorTo.length));
        if (retextureFrom != null)
            copy.setRetextureFrom(Arrays.copyOf(retextureFrom, retextureFrom.length));
        if (retextureTo != null)
            copy.setRetextureTo(Arrays.copyOf(retextureTo, retextureTo.length));
        return copy;
    }

    @Override
    public String toString() {
        return "GraphicsDefinitions(id=" + this.getId() + ", modelId=" + this.getModelId() + ", animationId=" + this.getAnimationId() + ", resizeX=" + this.getWidthScale() + ", resizeY=" + this.getHeightScale() + ", rotation=" + this.getOrientation() + ", ambience=" + this.getAmbient() + ", contrast=" + this.getContrast() + ", originalColours=" + Arrays.toString(this.getRecolorFrom()) + ", retextureToFind=" + Arrays.toString(this.getRetextureFrom()) + ", replacementColours=" + Arrays.toString(this.getRecolorTo()) + ", retextureToReplace=" + Arrays.toString(this.getRetextureTo()) + ")";
    }
}
