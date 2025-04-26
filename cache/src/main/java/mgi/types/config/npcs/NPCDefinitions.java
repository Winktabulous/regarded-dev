package mgi.types.config.npcs;

import com.esotericsoftware.kryo.Kryo;
import com.zenyte.CacheManager;
import com.zenyte.game.world.entity.masks.RenderType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.types.config.TransmogrifiableType;
import mgi.utilities.ByteBuffer;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public final class NPCDefinitions implements Definitions, Cloneable, TransmogrifiableType, RenderType {

    private static NPCDefinitions[] definitions;
    private int id;
    private String name;
    private transient String lowercaseName;
    private String[] options;
    private String[] filteredOptions;
    private int filterFlag;
    private int varp;
    private int varbit;
    int[] transmogrifiedIds;
    int[] models;
    int[] chatModels;
    private int standAnimation;
    private int walkAnimation;
    private int rotate90Animation;
    private int rotate180Animation;
    private int rotate270Animation;
    private int size;
    private int combatLevel;
    private boolean minimapVisible;
    private boolean visible;
    private boolean clickable;
    private boolean clippedMovement;
    private boolean isFollower;
    private int resizeX;
    private int resizeY;
    private int direction;
    private int[] headIconArchiveIds;
    private short[] headIconSpriteIndex;
    private int ambience;
    private int contrast;
    short[] originalColours;
    short[] replacementColours;
    short[] originalTextures;
    short[] replacementTextures;
    private int rotateLeftAnimation;
    private int rotateRightAnimation;
    private Int2ObjectMap<Object> parameters;
    private int finalTransmogrification;
    public int field2039 = -1;
    public int field2040 = -1;
    public int field2059 = -1;
    public int field2042 = -1;
    public int field2043 = -1;
    public int field2065 = -1;
    public int field2045 = -1;
    public int field2057 = -1;
    private boolean oldFormat;
    public int height = -1;

    public int[] stats = {1, 1, 1, 1, 1, 1};

    @Override
    public int defaultId() {
        return finalTransmogrification;
    }

    public NPCDefinitions(final int id, final ByteBuffer buffer) {
        this(id, buffer, false);
    }

    public NPCDefinitions(final int id, final ByteBuffer buffer, boolean oldFormat) {
        this.id = id;
        this.oldFormat = oldFormat;
        setDefaults();
        decode(buffer);
    }

    public NPCDefinitions clone() throws CloneNotSupportedException {
        return (NPCDefinitions) super.clone();
    }

    public static NPCDefinitions get(final int id) {
        if (id < 0 || id >= definitions.length) {
            return null;
        }
        return definitions[id];
    }

    public static NPCDefinitions getOrThrow(final int id) {
        final NPCDefinitions definitions = NPCDefinitions.definitions[id];
        if (definitions == null) {
            throw new RuntimeException("NPCDefinitions missing for id: " + id);
        }
        return definitions;
    }

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        load(cache);
    }

    public void load(Cache cache) {
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group npcs = configs.findGroupByID(GroupType.NPC);
        definitions = new NPCDefinitions[65535/*npcs.getHighestFileId()*/];
        for (int id = 0; id < definitions.length; id++) {
            final File file = npcs.findFileByID(id);
            if (file == null) continue;
            final ByteBuffer buffer = file.getData();
            if (buffer == null) continue;
            buffer.setPosition(0);
            definitions[id] = new NPCDefinitions(id, buffer);
        }
    }

    private void setDefaults() {
        name = lowercaseName = "null";
        size = 1;
        standAnimation = -1;
        walkAnimation = -1;
        rotate180Animation = -1;
        rotate90Animation = -1;
        rotate270Animation = -1;
        rotateLeftAnimation = -1;
        rotateRightAnimation = -1;
        options = new String[5];
        filteredOptions = new String[5];
        minimapVisible = true;
        combatLevel = -1;
        resizeX = 128;
        resizeY = 128;
        visible = false;
        ambience = 0;
        contrast = 0;
        headIconArchiveIds = null;
        headIconSpriteIndex = null;
        direction = 32;
        varbit = -1;
        varp = -1;
        clickable = true;
        clippedMovement = true;
        isFollower = false;
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
            case 1: {
                final int size = buffer.readUnsignedByte();
                models = new int[size];
                for (int i = 0; i < size; i++) {
                    models[i] = buffer.readUnsignedShort();
                }
                return;
            }
            case 2:
                name = buffer.readString();
                lowercaseName = name.toLowerCase();
                return;
            case 12:
                size = buffer.readUnsignedByte();
                return;
            case 13:
                standAnimation = buffer.readUnsignedShortNo65535();
                return;
            case 14:
                walkAnimation = buffer.readUnsignedShortNo65535();
                return;
            case 15:
                rotateLeftAnimation = buffer.readUnsignedShortNo65535();
                return;
            case 16:
                rotateRightAnimation = buffer.readUnsignedShortNo65535();
                return;
            case 17:
                walkAnimation = buffer.readUnsignedShortNo65535();
                rotate180Animation = buffer.readUnsignedShortNo65535();
                rotate90Animation = buffer.readUnsignedShortNo65535();
                rotate270Animation = buffer.readUnsignedShortNo65535();
                return;
            case 18:
                buffer.readUnsignedShort(); // category
                return;
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
                options[opcode - 30] = buffer.readString();
                if (options[opcode - 30].equalsIgnoreCase("Hidden")) {
                    options[opcode - 30] = null;
                }
                filteredOptions[opcode - 30] = options[opcode - 30];
                return;
            case 40: {
                final int size = buffer.readUnsignedByte();
                originalColours = new short[size];
                replacementColours = new short[size];
                for (int i = 0; i < size; i++) {
                    originalColours[i] = (short) (buffer.readUnsignedShort());
                    replacementColours[i] = (short) (buffer.readUnsignedShort());
                }
                return;
            }
            case 41: {
                final int size = buffer.readUnsignedByte();
                originalTextures = new short[size];
                replacementTextures = new short[size];
                for (int i = 0; i < size; i++) {
                    originalTextures[i] = (short) (buffer.readUnsignedShort());
                    replacementTextures[i] = (short) (buffer.readUnsignedShort());
                }
                return;
            }
            case 60: {
                final int size = buffer.readUnsignedByte();
                chatModels = new int[size];
                for (int i = 0; i < size; i++) {
                    chatModels[i] = buffer.readUnsignedShort();
                }
                return;
            }
            case 74: stats[0] = buffer.readUnsignedShort(); return;
            case 75: stats[1] = buffer.readUnsignedShort(); return;
            case 76: stats[2] = buffer.readUnsignedShort(); return;
            case 77: stats[3] = buffer.readUnsignedShort(); return;
            case 78: stats[4] = buffer.readUnsignedShort(); return;
            case 79: stats[5] = buffer.readUnsignedShort(); return;
            case 93:
                minimapVisible = false;
                return;
            case 95:
                combatLevel = buffer.readUnsignedShort();
                return;
            case 97:
                resizeX = buffer.readUnsignedShort();
                return;
            case 98:
                resizeY = buffer.readUnsignedShort();
                return;
            case 99:
                visible = true;
                return;
            case 100:
                ambience = buffer.readByte();
                return;
            case 101:
                contrast = buffer.readByte();
                return;
            case 102:
                if (oldFormat) {
                     buffer.readUnsignedShort();
                } else {
                    int mask = buffer.readUnsignedByte();
                    int length = 0;

                    int bit = mask;
                    while (bit != 0) {
                        length++;
                        bit >>= 1;
                    }

                    this.headIconArchiveIds = new int[length];
                    this.headIconSpriteIndex = new short[length];

                    for (int id = 0; id < length; ++id) {
                        if ((mask & 1 << id) == 0) {
                            this.headIconArchiveIds[id] = -1;
                            this.headIconSpriteIndex[id] = -1;
                        } else {
                            this.headIconArchiveIds[id] = buffer.readBigSmart();
                            this.headIconSpriteIndex[id] = (short) buffer.readUnsignedSmartSub();
                        }
                    }
                }
                return;
            case 103:
                direction = buffer.readUnsignedShort();
                return;
            case 106:
            case 118: {
                varbit = buffer.readUnsignedShortNo65535();
                varp = buffer.readUnsignedShortNo65535();
                finalTransmogrification = -1;
                if (opcode == 118) {
                    finalTransmogrification = buffer.readUnsignedShortNo65535();
                }
                final int size = buffer.readUnsignedByte();
                transmogrifiedIds = new int[size + 2];
                for (int int_3 = 0; int_3 <= size; int_3++) {
                    transmogrifiedIds[int_3] = buffer.readUnsignedShortNo65535();
                }
                transmogrifiedIds[size + 1] = finalTransmogrification;
                return;
            }
            case 107:
                clippedMovement = false;
                return;
            case 109:
                clickable = false;
                return;
            case 111:
                isFollower = true;
                return;
            case 114:
                field2039 = buffer.readUnsignedShort();
                if (field2039 == 65535) {
                    field2039 = -1;
                }
                return;
            case 115:
                field2039 = buffer.readUnsignedShort();
                if (field2039 == 65535) {
                    field2039 = -1;
                }
                field2040 = buffer.readUnsignedShort();
                if (field2040 == 65535) {
                    field2040 = -1;
                }
                field2059 = buffer.readUnsignedShort();
                if (field2059 == 65535) {
                    field2059 = -1;
                }
                field2042 = buffer.readUnsignedShort();
                if (field2042 == 65535) {
                    field2042 = -1;
                }
                return;
            case 116:
                field2043 = buffer.readUnsignedShort();
                if (field2043 == 65535) {
                    field2043 = -1;
                }
                return;
            case 117:
                field2043 = buffer.readUnsignedShort();
                if (field2043 == 65535) {
                    field2043 = -1;
                }
                field2065 = buffer.readUnsignedShort();
                if (field2065 == 65535) {
                    field2065 = -1;
                }
                field2045 = buffer.readUnsignedShort();
                if (field2045 == 65535) {
                    field2045 = -1;
                }
                field2057 = buffer.readUnsignedShort();
                if (field2057 == 65535) {
                    field2057 = -1;
                }
                return;
            case 124:
                height = buffer.readUnsignedShort();
                return;
            case 249:
                parameters = buffer.readParameters();
                return;
            //default:
            //    throw new RuntimeException("UNKNOWN NPC OPCODE: " + opcode);
        }
    }

    public String getOption(final int option) {
        if (options == null || options.length < option || option == 0) {
            return "";
        }
        return options[option - 1];
    }

    public boolean containsOptionCaseSensitive(final String option) {
        return ArrayUtils.contains(options, option);
    }

    public boolean containsOption(final String o) {
        if (options == null) {
            return false;
        }
        for (final String option : options) {
            if (option == null) {
                continue;
            }
            if (option.equalsIgnoreCase(o)) {
                return true;
            }
        }
        return false;
    }

    public NPCDefinitions(int id, String name, String lowercaseName, String[] options, String[] filteredOptions,
                          int filterFlag, int varp, int varbit, int[] transmogrifiedIds, int[] models,
                          int[] chatModels, int standAnimation, int walkAnimation, int rotate90Animation,
                          int rotate180Animation, int rotate270Animation, int size, int combatLevel,
                          boolean minimapVisible, boolean visible, boolean clickable, boolean clippedMovement,
                          boolean isFamiliar, int resizeX, int resizeY, int direction, int ambience,
                          int contrast, short[] originalColours, short[] replacementColours, short[] originalTextures,
                          short[] replacementTextures, int rotateLeftAnimation, int rotateRightAnimation,
                          Int2ObjectMap<Object> parameters, int finalTransmogrification, int[] headIconArchiveIds, short[] headIconSpriteIndex) {
        this.id = id;
        this.name = name;
        this.lowercaseName = lowercaseName;
        this.options = options;
        this.filteredOptions = filteredOptions;
        this.filterFlag = filterFlag;
        this.varp = varp;
        this.varbit = varbit;
        this.transmogrifiedIds = transmogrifiedIds;
        this.models = models;
        this.chatModels = chatModels;
        this.standAnimation = standAnimation;
        this.walkAnimation = walkAnimation;
        this.rotate90Animation = rotate90Animation;
        this.rotate180Animation = rotate180Animation;
        this.rotate270Animation = rotate270Animation;
        this.size = size;
        this.combatLevel = combatLevel;
        this.minimapVisible = minimapVisible;
        this.visible = visible;
        this.clickable = clickable;
        this.clippedMovement = clippedMovement;
        this.isFollower = isFamiliar;
        this.resizeX = resizeX;
        this.resizeY = resizeY;
        this.direction = direction;
        this.headIconArchiveIds = headIconArchiveIds;
        this.headIconSpriteIndex = headIconSpriteIndex;
        this.ambience = ambience;
        this.contrast = contrast;
        this.originalColours = originalColours;
        this.replacementColours = replacementColours;
        this.originalTextures = originalTextures;
        this.replacementTextures = replacementTextures;
        this.rotateLeftAnimation = rotateLeftAnimation;
        this.rotateRightAnimation = rotateRightAnimation;
        this.parameters = parameters;
        this.finalTransmogrification = finalTransmogrification;
    }

    public NPCDefinitions() {
    }

    @Override
    public void pack() {
        definitions[id] = this;
        try {
            pack(id, encode());
        } catch (Exception e) {
            logger.error("Failed to pack NPCDefinition with id: {}", id);
        }
    }

    public static void pack(int id, ByteBuffer buffer) {
        CacheManager.getCache()
                .getArchive(ArchiveType.CONFIGS)
                .findGroupByID(GroupType.NPC)
                .addFile(new File(id, buffer));
    }

    @Override
    public int getVarbitId() {
        return varbit;
    }

    @Override
    public int getVarpId() {
        return varp;
    }

    @Override
    public int getStand() {
        return this.standAnimation;
    }

    @Override
    public int getStandTurn() {
        return -1;
    }

    @Override
    public int getWalk() {
        return this.walkAnimation;
    }

    @Override
    public int getRotate180() {
        return this.rotate180Animation;
    }

    @Override
    public int getRotate90() {
        return this.rotate90Animation;
    }

    @Override
    public int getRotate270() {
        return this.rotate270Animation;
    }

    @Override
    public int getRun() {
        return this.walkAnimation;
    }

    public void setOption(final int index, final String option) {
        if (options == null) {
            options = new String[5];
        }
        options[index] = option.isEmpty() ? null : option;
    }

    public void setFilteredOption(final int index, final String option) {
        if (filteredOptions == null) {
            filteredOptions = new String[5];
        }
        filteredOptions[index] = option.isEmpty() ? null : option;
        filterFlag |= 1 << index;
    }

    public NPCDefinitions copy() {
        Kryo kryo = new Kryo();
        kryo.register(NPCDefinitions.class);
        kryo.register(int[].class);
        kryo.register(short[].class);
        kryo.register(String[].class);
        return kryo.copy(this);
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(4 * 1024);
        if (models != null) {
            buffer.writeByte(1);
            buffer.writeByte(models.length);
            for (int index = 0; index < models.length; index++) {
                buffer.writeShort(models[index]);
            }
        }
        if (!name.equals("null")) {
            buffer.writeByte(2);
            buffer.writeString(name);
        }
        if (size != 0) {
            buffer.writeByte(12);
            buffer.writeByte(size);
        }
        if (standAnimation != -1) {
            buffer.writeByte(13);
            buffer.writeShort(standAnimation == 65535 ? -1 : standAnimation);
        }
        final boolean extendedWalkAnimations =
                (rotate90Animation & 65535) != 65535 || (rotate180Animation & 65535) != 65535 || (rotate270Animation & 65535) != 65535;
        if (walkAnimation != -1 && !extendedWalkAnimations) {
            buffer.writeByte(14);
            buffer.writeShort(walkAnimation == 65535 ? -1 : walkAnimation);
        }
        if (rotateLeftAnimation != -1) {
            buffer.writeByte(15);
            buffer.writeShort(rotateLeftAnimation);
        }
        if (rotateRightAnimation != -1) {
            buffer.writeByte(16);
            buffer.writeShort(rotateRightAnimation);
        }
        if (extendedWalkAnimations) {
            buffer.writeByte(17);
            buffer.writeShort(walkAnimation == 65535 ? -1 : walkAnimation);
            buffer.writeShort(rotate180Animation == 65535 ? -1 : rotate180Animation);
            buffer.writeShort(rotate90Animation == 65535 ? -1 : rotate90Animation);
            buffer.writeShort(rotate270Animation == 65535 ? -1 : rotate270Animation);
        }
        for (int index = 0; index < 5; index++) {
            if (options[index] != null && !options[index].equals("Hidden")) {
                buffer.writeByte((30 + index));
                buffer.writeString(options[index]);
            }
        }
        if (originalColours != null && replacementColours != null && originalColours.length != 0 && replacementColours.length != 0) {
            buffer.writeByte(40);
            buffer.writeByte(originalColours.length);
            for (int index = 0; index < originalColours.length; index++) {
                buffer.writeShort(originalColours[index]);
                buffer.writeShort(replacementColours[index]);
            }
        }
        if (originalTextures != null && replacementTextures != null && originalTextures.length != 0 && replacementTextures.length != 0) {
            buffer.writeByte(41);
            buffer.writeByte(originalTextures.length);
            for (int index = 0; index < originalTextures.length; index++) {
                buffer.writeShort(originalTextures[index]);
                buffer.writeShort(replacementTextures[index]);
            }
        }
        if (chatModels != null) {
            buffer.writeByte(60);
            buffer.writeByte(chatModels.length);
            for (int index = 0; index < chatModels.length; index++) {
                buffer.writeShort(chatModels[index]);
            }
        }
        if (!minimapVisible) {
            buffer.writeByte(93);
        }
        if (combatLevel != -1) {
            buffer.writeByte(95);
            buffer.writeShort(combatLevel);
        }
        if (resizeX != 0) {
            buffer.writeByte(97);
            buffer.writeShort(resizeX);
        }
        if (resizeY != 0) {
            buffer.writeByte(98);
            buffer.writeShort(resizeY);
        }
        if (visible) {
            buffer.writeByte(99);
        }
        if (ambience != 0) {
            buffer.writeByte(100);
            buffer.writeByte(ambience);
        }
        if (contrast != 0) {
            buffer.writeByte(101);
            buffer.writeByte(contrast);
        }
        if (headIconArchiveIds != null && headIconArchiveIds.length > 0) {
            buffer.writeByte(102);

            int mask = 0;
            for (int i = 0; i < headIconArchiveIds.length; i++) {
                if (headIconArchiveIds[i] != -1) {
                    mask |= 1 << i;
                }
            }

            buffer.writeByte(mask);

            for(int id = 0; id < headIconArchiveIds.length; ++id) {
                if (headIconArchiveIds[id] != -1) {
                    buffer.writeShort(headIconArchiveIds[id]);
                    buffer.writeSmart(headIconSpriteIndex[id] + 1);
                }
            }
        }
        if (direction != -1) {
            buffer.writeByte(103);
            buffer.writeShort(direction);
        }
        if (!clippedMovement) {
            buffer.writeByte(107);
        }
        if (!clickable) {
            buffer.writeByte(109);
        }
        if (isFollower) {
            buffer.writeByte(111);
        }

        final boolean extendedAnimations1 = field2040 != -1 || field2059 != -1 || field2042 != -1;
        if (field2039 != -1 && !extendedAnimations1) {
            buffer.writeByte(114);
            buffer.writeShort(field2039);
        }
        if (extendedAnimations1) {
            buffer.writeByte(115);
            buffer.writeShort(field2039);
            buffer.writeShort(field2040);
            buffer.writeShort(field2059);
            buffer.writeShort(field2042);
        }

        final boolean extendedAnimations2 = field2065 != -1 || field2045 != -1 || field2057 != -1;
        if (field2043 != -1 && !extendedAnimations2) {
            buffer.writeByte(116);
            buffer.writeShort(field2043);
        }
        if (extendedAnimations2) {
            buffer.writeByte(117);
            buffer.writeShort(field2043);
            buffer.writeShort(field2065);
            buffer.writeShort(field2045);
            buffer.writeShort(field2057);
        }

        if (transmogrifiedIds != null && transmogrifiedIds.length > 0) {
            buffer.writeByte(106);
            buffer.writeShort(varbit == -1 ? 65535 : varbit);
            buffer.writeShort(varp == -1 ? 65535 : varp);
            buffer.writeByte(transmogrifiedIds.length - 2);
            for (int index = 0; index <= transmogrifiedIds.length - 2; index++) {
                buffer.writeShort(transmogrifiedIds[index] == -1 ? 65535 : transmogrifiedIds[index]);
            }
        }
        if (transmogrifiedIds != null) {
            buffer.writeByte(118);
            buffer.writeShort(varbit == -1 ? 65535 : varbit);
            buffer.writeShort(varp == -1 ? 65535 : varp);
            buffer.writeShort(transmogrifiedIds[transmogrifiedIds.length - 1] == -1 ? 65535 :
                    transmogrifiedIds[transmogrifiedIds.length - 1]);
            buffer.writeByte(transmogrifiedIds.length - 2);
            for (int index = 0; index <= transmogrifiedIds.length - 2; index++) {
                buffer.writeShort(transmogrifiedIds[index] == -1 ? 65535 : transmogrifiedIds[index]);
            }
        }
        if (parameters != null && !parameters.isEmpty()) {
            buffer.writeByte(249);
            buffer.writeParameters(parameters);
        }
        buffer.writeByte(0);
        return buffer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLowercaseName() {
        return lowercaseName;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String[] getFilteredOptions() {
        return filteredOptions;
    }

    public void setFilteredOptions(String[] filteredOptions) {
        this.filteredOptions = filteredOptions;
    }

    public int getFilterFlag() {
        return filterFlag;
    }

    public int getVarp() {
        return varp;
    }

    public void setVarp(int varp) {
        this.varp = varp;
    }

    public int getVarbit() {
        return varbit;
    }

    public void setVarbit(int varbit) {
        this.varbit = varbit;
    }

    public int[] getTransmogrifiedIds() {
        return transmogrifiedIds;
    }

    public void setTransmogrifiedIds(int[] transmogrifiedIds) {
        this.transmogrifiedIds = transmogrifiedIds;
    }

    public int[] getModels() {
        return models;
    }

    public void setModels(int[] models) {
        this.models = models;
    }

    public int[] getChatModels() {
        return chatModels;
    }

    public void setChatModels(int[] chatModels) {
        this.chatModels = chatModels;
    }

    public int getStandAnimation() {
        return standAnimation;
    }

    public void setStandAnimation(int standAnimation) {
        this.standAnimation = standAnimation;
    }

    public int getWalkAnimation() {
        return walkAnimation;
    }

    public void setWalkAnimation(int walkAnimation) {
        this.walkAnimation = walkAnimation;
    }

    public int getRotate90Animation() {
        return rotate90Animation;
    }

    public void setRotate90Animation(int rotate90Animation) {
        this.rotate90Animation = rotate90Animation;
    }

    public int getRotate180Animation() {
        return rotate180Animation;
    }

    public void setRotate180Animation(int rotate180Animation) {
        this.rotate180Animation = rotate180Animation;
    }

    public int getRotate270Animation() {
        return rotate270Animation;
    }

    public void setRotate270Animation(int rotate270Animation) {
        this.rotate270Animation = rotate270Animation;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    public boolean isMinimapVisible() {
        return minimapVisible;
    }

    public void setMinimapVisible(boolean minimapVisible) {
        this.minimapVisible = minimapVisible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isClippedMovement() {
        return clippedMovement;
    }

    public void setClippedMovement(boolean clippedMovement) {
        this.clippedMovement = clippedMovement;
    }

    public boolean isFamiliar() {
        return isFollower;
    }

    public void setFamiliar(boolean isFamiliar) {
        this.isFollower = isFamiliar;
    }

    public int getResizeX() {
        return resizeX;
    }

    public void setResizeX(int resizeX) {
        this.resizeX = resizeX;
    }

    public int getResizeY() {
        return resizeY;
    }

    public void setResizeY(int resizeY) {
        this.resizeY = resizeY;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getAmbience() {
        return ambience;
    }

    public void setAmbience(int ambience) {
        this.ambience = ambience;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public short[] getOriginalColours() {
        return originalColours;
    }

    public void setOriginalColours(short[] originalColours) {
        this.originalColours = originalColours;
    }

    public short[] getReplacementColours() {
        return replacementColours;
    }

    public void setReplacementColours(short[] replacementColours) {
        this.replacementColours = replacementColours;
    }

    public short[] getOriginalTextures() {
        return originalTextures;
    }

    public void setOriginalTextures(short[] originalTextures) {
        this.originalTextures = originalTextures;
    }

    public short[] getReplacementTextures() {
        return replacementTextures;
    }

    public void setReplacementTextures(short[] replacementTextures) {
        this.replacementTextures = replacementTextures;
    }

    public int getRotateLeftAnimation() {
        return rotateLeftAnimation;
    }

    public void setRotateLeftAnimation(int rotateLeftAnimation) {
        this.rotateLeftAnimation = rotateLeftAnimation;
    }

    public int getRotateRightAnimation() {
        return rotateRightAnimation;
    }

    public void setRotateRightAnimation(int rotateRightAnimation) {
        this.rotateRightAnimation = rotateRightAnimation;
    }

    public Int2ObjectMap<Object> getParameters() {
        return parameters;
    }

    public void setParameters(Int2ObjectMap<Object> parameters) {
        this.parameters = parameters;
    }

    public int getFinalTransmogrification() {
        return finalTransmogrification;
    }


    public static class NPCDefinitionsBuilder {
        private int id;
        private String name;
        private String lowercaseName;
        private String[] options;
        private String[] filteredOptions;
        private int filterFlag;
        private int varp;
        private int varbit;
        private int[] transmogrifiedIds;
        private int[] models;
        private int[] chatModels;
        private int standAnimation;
        private int walkAnimation;
        private int rotate90Animation;
        private int rotate180Animation;
        private int rotate270Animation;
        private int size;
        private int combatLevel;
        private boolean minimapVisible;
        private boolean visible;
        private boolean clickable;
        private boolean clippedMovement;
        private boolean isFamiliar;
        private int resizeX;
        private int resizeY;
        private int direction;
        private int ambience;
        private int contrast;
        private short[] originalColours;
        private short[] replacementColours;
        private short[] originalTextures;
        private short[] replacementTextures;
        private int field3568;
        private int field3580;
        private Int2ObjectMap<Object> parameters;
        private int finalTransmogrification;
        private int[] headIconArchiveIds;
        private short[] headIconSpriteIndex;
        private int field2039;
        private int field2040;
        private int field2059;
        private int field2042;
        private int field2043;
        private int field2065;
        private int field2045;
        private int field2057;

        NPCDefinitionsBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder id(final int id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder lowercaseName(final String lowercaseName) {
            this.lowercaseName = lowercaseName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder options(final String[] options) {
            this.options = options;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder filteredOptions(final String[] filteredOptions) {
            this.filteredOptions = filteredOptions;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder filterFlag(final int filterFlag) {
            this.filterFlag = filterFlag;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder varp(final int varp) {
            this.varp = varp;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder varbit(final int varbit) {
            this.varbit = varbit;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder transmogrifiedIds(final int[] transmogrifiedIds) {
            this.transmogrifiedIds = transmogrifiedIds;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder models(final int[] models) {
            this.models = models;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder chatModels(final int[] chatModels) {
            this.chatModels = chatModels;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder standAnimation(final int standAnimation) {
            this.standAnimation = standAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder walkAnimation(final int walkAnimation) {
            this.walkAnimation = walkAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder rotate90Animation(final int rotate90Animation) {
            this.rotate90Animation = rotate90Animation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder rotate180Animation(final int rotate180Animation) {
            this.rotate180Animation = rotate180Animation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder rotate270Animation(final int rotate270Animation) {
            this.rotate270Animation = rotate270Animation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder size(final int size) {
            this.size = size;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder combatLevel(final int combatLevel) {
            this.combatLevel = combatLevel;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder minimapVisible(final boolean minimapVisible) {
            this.minimapVisible = minimapVisible;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder visible(final boolean visible) {
            this.visible = visible;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder clickable(final boolean clickable) {
            this.clickable = clickable;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder clippedMovement(final boolean clippedMovement) {
            this.clippedMovement = clippedMovement;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder isFamiliar(final boolean isFamiliar) {
            this.isFamiliar = isFamiliar;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder resizeX(final int resizeX) {
            this.resizeX = resizeX;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder resizeY(final int resizeY) {
            this.resizeY = resizeY;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder direction(final int direction) {
            this.direction = direction;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder ambience(final int ambience) {
            this.ambience = ambience;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder contrast(final int contrast) {
            this.contrast = contrast;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder originalColours(final short[] originalColours) {
            this.originalColours = originalColours;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder replacementColours(final short[] replacementColours) {
            this.replacementColours = replacementColours;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder originalTextures(final short[] originalTextures) {
            this.originalTextures = originalTextures;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder replacementTextures(final short[] replacementTextures) {
            this.replacementTextures = replacementTextures;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder field3568(final int field3568) {
            this.field3568 = field3568;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder field3580(final int field3580) {
            this.field3580 = field3580;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder parameters(final Int2ObjectMap<Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField2039(int field2039) {
            this.field2039 = field2039;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField2040(int field2040) {
            this.field2040 = field2040;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField2042(int field2042) {
            this.field2042 = field2042;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField2043(int field2043) {
            this.field2043 = field2043;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField2045(int field2045) {
            this.field2045 = field2045;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField2057(int field2057) {
            this.field2057 = field2057;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField2059(int field2059) {
            this.field2059 = field2059;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField2065(int field2065) {
            this.field2065 = field2065;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField3568(int field3568) {
            this.field3568 = field3568;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setField3580(int field3580) {
            this.field3580 = field3580;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setHeadIconArchiveIds(int[] headIconArchiveIds) {
            this.headIconArchiveIds = headIconArchiveIds;
            return this;
        }

        public NPCDefinitions.NPCDefinitionsBuilder setHeadIconSpriteIndex(short[] headIconSpriteIndex) {
            this.headIconSpriteIndex = headIconSpriteIndex;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public NPCDefinitions.NPCDefinitionsBuilder finalTransmogrification(final int finalTransmogrification) {
            this.finalTransmogrification = finalTransmogrification;
            return this;
        }

        public NPCDefinitions build() {
            return new NPCDefinitions(this.id, this.name, this.lowercaseName, this.options, this.filteredOptions,
                    this.filterFlag, this.varp, this.varbit, this.transmogrifiedIds, this.models, this.chatModels,
                    this.standAnimation, this.walkAnimation, this.rotate90Animation, this.rotate180Animation,
                    this.rotate270Animation, this.size, this.combatLevel, this.minimapVisible, this.visible,
                    this.clickable, this.clippedMovement, this.isFamiliar, this.resizeX, this.resizeY, this.direction,
                    this.ambience, this.contrast, this.originalColours, this.replacementColours,
                    this.originalTextures, this.replacementTextures, this.field3568, this.field3580, this.parameters,
                    this.finalTransmogrification, this.headIconArchiveIds, this.headIconSpriteIndex);
        }

        @Override
        public String toString() {
            return "NPCDefinitions.NPCDefinitionsBuilder(id=" + this.id + ", name=" + this.name + ", lowercaseName=" + this.lowercaseName + ", options=" + Arrays.deepToString(this.options) + ", filteredOptions=" + Arrays.deepToString(this.filteredOptions) + ", filterFlag=" + this.filterFlag + ", varp=" + this.varp + ", varbit=" + this.varbit + ", transmogrifiedIds=" + Arrays.toString(this.transmogrifiedIds) + ", models=" + Arrays.toString(this.models) + ", chatModels=" + Arrays.toString(this.chatModels) + ", standAnimation=" + this.standAnimation + ", walkAnimation=" + this.walkAnimation + ", rotate90Animation=" + this.rotate90Animation + ", rotate180Animation=" + this.rotate180Animation + ", rotate270Animation=" + this.rotate270Animation + ", size=" + this.size + ", combatLevel=" + this.combatLevel + ", minimapVisible=" + this.minimapVisible + ", visible=" + this.visible + ", clickable=" + this.clickable + ", clippedMovement=" + this.clippedMovement + ", isFamiliar=" + this.isFamiliar + ", resizeX=" + this.resizeX + ", resizeY=" + this.resizeY + ", direction=" + this.direction + ", ambience=" + this.ambience + ", contrast=" + this.contrast + ", originalColours=" + Arrays.toString(this.originalColours) + ", replacementColours=" + Arrays.toString(this.replacementColours) + ", originalTextures=" + Arrays.toString(this.originalTextures) + ", replacementTextures=" + Arrays.toString(this.replacementTextures) + ", field3568=" + this.field3568 + ", field3580=" + this.field3580 + ", parameters=" + this.parameters + ", finalTransmogrification=" + this.finalTransmogrification + ")";
        }
    }

    public static NPCDefinitions.NPCDefinitionsBuilder builder() {
        return new NPCDefinitions.NPCDefinitionsBuilder();
    }

    public NPCDefinitions.NPCDefinitionsBuilder toBuilder() {
        return new NPCDefinitions.NPCDefinitionsBuilder().id(this.id).name(this.name).lowercaseName(this.lowercaseName).options(this.options).filteredOptions(this.filteredOptions).filterFlag(this.filterFlag).varp(this.varp).varbit(this.varbit).transmogrifiedIds(this.transmogrifiedIds).models(this.models).chatModels(this.chatModels).standAnimation(this.standAnimation).walkAnimation(this.walkAnimation).rotate90Animation(this.rotate90Animation).rotate180Animation(this.rotate180Animation).rotate270Animation(this.rotate270Animation).size(this.size).combatLevel(this.combatLevel).minimapVisible(this.minimapVisible).visible(this.visible).clickable(this.clickable).clippedMovement(this.clippedMovement).isFamiliar(this.isFollower).resizeX(this.resizeX).resizeY(this.resizeY).direction(this.direction).ambience(this.ambience).contrast(this.contrast).originalColours(this.originalColours).replacementColours(this.replacementColours).originalTextures(this.originalTextures).replacementTextures(this.replacementTextures).field3568(this.rotateLeftAnimation).field3580(this.rotateRightAnimation).parameters(this.parameters).finalTransmogrification(this.finalTransmogrification);
    }

    @Override
    public String toString() {
        return "NPCDefinitions{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lowercaseName='" + lowercaseName + '\'' +
                ", options=" + Arrays.toString(options) +
                ", filteredOptions=" + Arrays.toString(filteredOptions) +
                ", filterFlag=" + filterFlag +
                ", varp=" + varp +
                ", varbit=" + varbit +
                ", transmogrifiedIds=" + Arrays.toString(transmogrifiedIds) +
                ", models=" + Arrays.toString(models) +
                ", chatModels=" + Arrays.toString(chatModels) +
                ", standAnimation=" + standAnimation +
                ", walkAnimation=" + walkAnimation +
                ", rotate90Animation=" + rotate90Animation +
                ", rotate180Animation=" + rotate180Animation +
                ", rotate270Animation=" + rotate270Animation +
                ", size=" + size +
                ", combatLevel=" + combatLevel +
                ", minimapVisible=" + minimapVisible +
                ", visible=" + visible +
                ", clickable=" + clickable +
                ", clippedMovement=" + clippedMovement +
                ", isFamiliar=" + isFollower +
                ", resizeX=" + resizeX +
                ", resizeY=" + resizeY +
                ", direction=" + direction +
                ", headIconArchiveIds=" + Arrays.toString(headIconArchiveIds) +
                ", headIconSpriteIndex=" + Arrays.toString(headIconSpriteIndex) +
                ", ambience=" + ambience +
                ", contrast=" + contrast +
                ", originalColours=" + Arrays.toString(originalColours) +
                ", replacementColours=" + Arrays.toString(replacementColours) +
                ", originalTextures=" + Arrays.toString(originalTextures) +
                ", replacementTextures=" + Arrays.toString(replacementTextures) +
                ", rotateLeftAnimation=" + rotateLeftAnimation +
                ", rotateRightAnimation=" + rotateRightAnimation +
                ", parameters=" + parameters +
                ", finalTransmogrification=" + finalTransmogrification +
                ", field2039=" + field2039 +
                ", field2040=" + field2040 +
                ", field2059=" + field2059 +
                ", field2042=" + field2042 +
                ", field2043=" + field2043 +
                ", field2065=" + field2065 +
                ", field2045=" + field2045 +
                ", field2057=" + field2057 +
                '}';
    }

    public static NPCDefinitions[] getDefinitions() {
        return definitions;
    }

}
