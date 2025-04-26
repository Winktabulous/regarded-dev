package mgi.types.config.identitykit;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 27. veebr 2018 : 2:10.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 * profile</a>}
 */
public final class IdentityKitDefinitions implements Definitions {

    public static IdentityKitDefinitions[] definitions;
    private static final List<Integer> hairStyles = new ArrayList<>(25);
    private static final List<Integer> beardStyles = new ArrayList<>(16);
    private static final List<Integer> bodyStyles = new ArrayList<>(15);
    private static final List<Integer> armStyles = new ArrayList<>(13);
    private static final List<Integer> legsStyle = new ArrayList<>(12);

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group identityKits = configs.findGroupByID(GroupType.IDENTKIT);
        definitions = new IdentityKitDefinitions[identityKits.getHighestFileId()];
        for (int id = 0; id < identityKits.getHighestFileId(); id++) {
            final File file = identityKits.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            definitions[id] = new IdentityKitDefinitions(id, buffer);
        }
    }

    private final int id;
    private int[] headModels;
    private int[] modelIds;
    private int bodyPartId;
    private boolean selectable;
    private short[] originalColours;
    private short[] originalTextures;
    private short[] replacementColours;
    private short[] replacementTextures;

    private IdentityKitDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        setDefaults();
        decode(buffer);
        if (selectable) {
            final int part = bodyPartId;
            if (part == 0) {
                hairStyles.add(id);
            } else if (part == 1) {
                beardStyles.add(id);
            } else if (part == 2) {
                bodyStyles.add(id);
            } else if (part == 3) {
                armStyles.add(id);
            } else if (part == 5) {
                legsStyle.add(id);
            }
        }
    }

    private void setDefaults() {
        bodyPartId = -1;
        headModels = new int[]{-1, -1, -1, -1, -1};
        selectable = true;
    }

    public static IdentityKitDefinitions get(final int id) {
        if (id < 0 || id >= definitions.length) {
            return null;
        }
        return definitions[id];
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
            case 1:
                bodyPartId = buffer.readUnsignedByte();
                return;
            case 2:
                modelIds = new int[buffer.readUnsignedByte()];
                for (int i = 0; i < modelIds.length; i++) {
                    modelIds[i] = buffer.readUnsignedShort();
                }
                return;
            case 3:
                selectable = false;
                return;
            case 40: {
                final int length = buffer.readUnsignedByte();
                originalColours = new short[length];
                replacementColours = new short[length];
                for (int i = 0; i < length; i++) {
                    originalColours[i] = (short) buffer.readUnsignedShort();
                    replacementColours[i] = (short) buffer.readUnsignedShort();
                }
                return;
            }
            case 41: {
                final int length = buffer.readUnsignedByte();
                originalTextures = new short[length];
                replacementTextures = new short[length];
                for (int i = 0; i < length; i++) {
                    originalTextures[i] = (short) buffer.readUnsignedShort();
                    replacementTextures[i] = (short) buffer.readUnsignedShort();
                }
                return;
            }
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
                headModels[opcode - 60] = buffer.readUnsignedShort();
                return;
        }
    }

    public static int getHairstyle(final int index) {
        if (index < 0 || index >= hairStyles.size()) {
            return 0;
        }
        return hairStyles.get(index);
    }

    public static int getBeardstyle(final int index) {
        if (index < 0 || index >= beardStyles.size()) {
            return 0;
        }
        return beardStyles.get(index);
    }

    public static int getBodystyle(final int index) {
        if (index < 0 || index >= bodyStyles.size()) {
            return 0;
        }
        return bodyStyles.get(index);
    }

    public static int getLegsstyle(final int index) {
        if (index < 0 || index >= legsStyle.size()) {
            return 0;
        }
        return legsStyle.get(index);
    }

    public static int getArmstyle(final int index) {
        if (index < 0 || index >= armStyles.size()) {
            return 0;
        }
        return armStyles.get(index);
    }

    public IdentityKitDefinitions() {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public int[] getHeadModels() {
        return headModels;
    }

    public int[] getModelIds() {
        return modelIds;
    }

    public int getBodyPartId() {
        return bodyPartId;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public short[] getOriginalColours() {
        return originalColours;
    }

    public short[] getOriginalTextures() {
        return originalTextures;
    }

    public short[] getReplacementColours() {
        return replacementColours;
    }

    public short[] getReplacementTextures() {
        return replacementTextures;
    }
    
}
