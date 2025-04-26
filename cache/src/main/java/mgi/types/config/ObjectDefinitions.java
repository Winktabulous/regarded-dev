package mgi.types.config;

import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;

import java.util.Arrays;

import static mgi.types.config.ObjectDefinitionsDecoding.decodeOpcode;

/**
 * @author Kris | 12. dets 2017 : 1:19.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ObjectDefinitions implements Definitions, Cloneable, TransmogrifiableType {

    private int id;
    private String name;
    public static ObjectDefinitions[] definitions;
    private int varbit;
    private int optionsInvisible;
    int[] models;
    int[] types;
    int[] transformedIds;
    private int ambientSoundId;
    int varp;
    int supportItems;
    int[] anIntArray100;
    int mapIconId;
    private int sizeX;
    private int clipType;
    public boolean isRotated;
    private int sizeY;
    private boolean projectileClip;
    int ambientSoundDistance;
    boolean nonFlatShading;
    private int contouredGround;
    int anInt456;
    public boolean modelClipped;
    private int ambient;
    private String[] options;
    private int contrast;
    int anInt457;
    boolean hollow;
    private int animationId;
    private int modelSizeX;
    public int decorDisplacement;
    private int modelSizeHeight;
    private int modelSizeY;
    int[] modelColours;
    boolean clipped;
    private short[] modelTexture;
    private int mapSceneId;
    int[] replacementColours;
    private int offsetX;
    private short[] replacementTexture;
    private int offsetHeight;
    private int offsetY;
    boolean obstructsGround;
    private int accessBlockFlag;
    private int finalTransformation;
    private Int2ObjectMap<Object> parameters;

    public ObjectDefinitions() {
        setDefaults();
    }

    public ObjectDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        setDefaults();
        decode(buffer);
    }

    public static ObjectDefinitions getOrThrow(final int id) {
        final ObjectDefinitions object = get(id);
        if (object == null) {
            throw new IllegalStateException();
        }
        return object;
    }

    public static ObjectDefinitions get(final int id) {
        if (id < 0 || id >= definitions.length) {
            return null;
        }
        return definitions[id];
    }

    public static final int REV_220_OBJ_ARCHIVE_REV = 1673;


    public void copy(int id) {
        ObjectDefinitions copy = get(id);
        if(copy == null)
            throw new RuntimeException("Object not found");

        this.name = copy.name;
        this.varbit = copy.varbit;
        this.optionsInvisible = copy.optionsInvisible;
        this.models = copy.models;
        this.types = copy.types;
        this.transformedIds = copy.transformedIds;
        this.ambientSoundId = copy.ambientSoundId;
        this.varp = copy.varp;
        this.supportItems = copy.supportItems;
        this.anIntArray100 = copy.anIntArray100;
        this.mapIconId = copy.mapIconId;
        this.sizeX = copy.sizeX;
        this.clipType = copy.clipType;
        this.isRotated = copy.isRotated;
        this.sizeY = copy.sizeY;
        this.projectileClip = copy.projectileClip;
        this.ambientSoundDistance = copy.ambientSoundDistance;
        this.nonFlatShading = copy.nonFlatShading;
        this.contouredGround = copy.contouredGround;
        this.anInt456 = copy.anInt456;
        this.modelClipped = copy.modelClipped;
        this.ambient = copy.ambient;
        this.options = copy.options;
        this.contrast = copy.contrast;
        this.anInt457 = copy.anInt457;
        this.hollow = copy.hollow;
        this.animationId = copy.animationId;
        this.modelSizeX = copy.modelSizeX;
        this.decorDisplacement = copy.decorDisplacement;
        this.modelSizeHeight = copy.modelSizeHeight;
        this.modelSizeY = copy.modelSizeY;
        this.modelColours = copy.modelColours;
        this.clipped = copy.clipped;
        this.modelTexture = copy.modelTexture;
        this.mapSceneId = copy.mapSceneId;
        this.replacementColours = copy.replacementColours;
        this.offsetX = copy.offsetX;
        this.replacementTexture = copy.replacementTexture;
        this.offsetHeight = copy.offsetHeight;
        this.offsetY = copy.offsetY;
        this.obstructsGround = copy.obstructsGround;
        this.accessBlockFlag = copy.accessBlockFlag;
        this.finalTransformation = copy.finalTransformation;
        this.parameters = copy.parameters;
    }

    @Override
    public int defaultId() {
        return finalTransformation;
    }

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group objects = configs.findGroupByID(GroupType.OBJECT);
        definitions = new ObjectDefinitions[objects.getHighestFileId()];//Hard cap at 40k for now.
        for (int id = 0; id < objects.getHighestFileId(); id++) {
            final File file = objects.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            definitions[id] = new ObjectDefinitions(id, buffer);
        }
    }

    private void setDefaults() {
        name = "null";
        sizeX = 1;
        sizeY = 1;
        clipType = 2;
        projectileClip = true;
        optionsInvisible = -1;
        contouredGround = -1;
        nonFlatShading = false;
        modelClipped = false;
        animationId = -1;
        decorDisplacement = 16;
        ambient = 0;
        contrast = 0;
        options = new String[5];
        mapIconId = -1;
        mapSceneId = -1;
        isRotated = false;
        clipped = true;
        modelSizeX = 128;
        modelSizeHeight = 128;
        modelSizeY = 128;
        offsetX = 0;
        offsetHeight = 0;
        offsetY = 0;
        obstructsGround = false;
        hollow = false;
        supportItems = -1;
        varbit = -1;
        varp = -1;
        ambientSoundId = -1;
        ambientSoundDistance = 0;
        anInt456 = 0;
        anInt457 = 0;
    }

    public boolean containsOption(final int i, final String option) {
        if (options == null || options[i] == null || options.length <= i) {
            return false;
        }
        return options[i].equals(option);
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
        decodeOpcode(this, buffer, opcode, this.id <= 47337);
    }

    public String getOption(final int option) {
        if (options == null || options.length < option || option == 0) {
            return "";
        }
        return options[option - 1];
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(2048);
        if (types != null) {
            buffer.writeByte(1);
            buffer.writeByte(types.length);
            for (int count = 0; count < types.length; count++) {
                buffer.writeShort(models[count]);
                buffer.writeByte(types[count]);
            }
        }
        if (!name.equals("null")) {
            buffer.writeByte(2);
            buffer.writeString(name);
        }
        if (models != null) {
            buffer.writeByte(5);
            buffer.writeByte(models.length);
            if (models.length > 0) {
                for (final int model : models) {
                    buffer.writeShort(model);
                }
            }
        }
        if (sizeX != 1) {
            buffer.writeByte(14);
            buffer.writeByte(sizeX);
        }
        if (sizeY != 1) {
            buffer.writeByte(15);
            buffer.writeByte(sizeY);
        }
        if (clipType == 0 && !projectileClip) {
            buffer.writeByte(17);
        }
        if (!projectileClip) {
            buffer.writeByte(18);
        }
        if (optionsInvisible != -1) {
            buffer.writeByte(19);
            buffer.writeByte(optionsInvisible);
        }
        if (contouredGround == 0) {
            buffer.writeByte(21);
        }
        if (nonFlatShading) {
            buffer.writeByte(22);
        }
        if (modelClipped) {
            buffer.writeByte(23);
        }
        if (animationId != -1) {
            buffer.writeByte(24);
            buffer.writeShort(animationId);
        }
        if (clipType == 1) {
            buffer.writeByte(27);
        }
        if (decorDisplacement != 16) {
            buffer.writeByte(28);
            buffer.writeByte(decorDisplacement);
        }
        if (ambient != 0) {
            buffer.writeByte(29);
            buffer.writeByte(ambient);
        }
        for (int index = 0; index < 5; ++index) {
            if (options[index] == null) {
                continue;
            }
            buffer.writeByte((30 + index));
            final String option = options[index];
            buffer.writeString(option);
        }
        if (contrast != 0) {
            buffer.writeByte(39);
            buffer.writeByte((contrast / 25));
        }
        if (modelColours != null && replacementColours != null && modelColours.length != 0 && replacementColours.length != 0) {
            buffer.writeByte(40);
            buffer.writeByte(modelColours.length);
            for (int index = 0; index < modelColours.length; ++index) {
                buffer.writeShort(modelColours[index]);
                buffer.writeShort(replacementColours[index]);
            }
        }
        if (modelTexture != null && replacementTexture != null && modelTexture.length != 0 && replacementTexture.length != 0) {
            buffer.writeByte(41);
            buffer.writeByte(modelTexture.length);
            for (int index = 0; index < modelTexture.length; ++index) {
                buffer.writeShort(modelTexture[index]);
                buffer.writeShort(replacementTexture[index]);
            }
        }
        if (isRotated) {
            buffer.writeByte(62);
        }
        if (!clipped) {
            buffer.writeByte(64);
        }
        if (modelSizeX != 128) {
            buffer.writeByte(65);
            buffer.writeShort(modelSizeX);
        }
        if (modelSizeHeight != 128) {
            buffer.writeByte(66);
            buffer.writeShort(modelSizeHeight);
        }
        if (modelSizeY != 128) {
            buffer.writeByte(67);
            buffer.writeShort(modelSizeY);
        }
        if (mapSceneId != -1) {
            buffer.writeByte(68);
            buffer.writeShort(mapSceneId);
        }
        if (accessBlockFlag != 0) {
            buffer.writeByte(69);
            buffer.writeByte(accessBlockFlag);
        }
        if (offsetX != 0) {
            buffer.writeByte(70);
            buffer.writeShort(offsetX);
        }
        if (offsetHeight != 0) {
            buffer.writeByte(71);
            buffer.writeShort(offsetHeight);
        }
        if (offsetY != 0) {
            buffer.writeByte(72);
            buffer.writeShort(offsetY);
        }
        if (obstructsGround) {
            buffer.writeByte(73);
        }
        if (hollow) {
            buffer.writeByte(74);
        }
        if (supportItems != -1) {
            buffer.writeByte(75);
            buffer.writeByte(supportItems);
        }
        if (ambientSoundId != -1) {
            buffer.writeByte(78);
            buffer.writeShort(ambientSoundId);
            buffer.writeByte(ambientSoundDistance);
        }
        if (anIntArray100 != null && anIntArray100.length != 0) {
            buffer.writeByte(79);
            buffer.writeShort(anInt456);
            buffer.writeShort(anInt457);
            buffer.writeByte(ambientSoundDistance);
            buffer.writeByte(anIntArray100.length);
            for (final int value : anIntArray100) {
                buffer.writeShort(value);
            }
        }
        if (contouredGround != -1) {
            buffer.writeByte(81);
            buffer.writeByte(contouredGround / 256);
        }
        if (mapIconId != -1) {
            buffer.writeByte(82);
            buffer.writeShort(mapIconId);
        }
        if (transformedIds != null) {
            buffer.writeByte(77);
            buffer.writeShort(varbit);
            buffer.writeShort(varp);
            buffer.writeByte((transformedIds.length - 2));
            for (int i = 0; i <= transformedIds.length - 2; ++i) {
                buffer.writeShort(transformedIds[i]);
            }
            buffer.writeByte(92);
            buffer.writeShort(varbit);
            buffer.writeShort(varp);
            buffer.writeShort(finalTransformation);
            buffer.writeByte((transformedIds.length - 2));
            for (int i = 0; i <= transformedIds.length - 2; ++i) {
                buffer.writeShort(transformedIds[i]);
            }
        }
        if (parameters != null && !parameters.isEmpty()) {
            buffer.writeByte(249);
            buffer.writeParameters(parameters);
        }
        buffer.writeByte(0);
        return buffer;
    }

    @Override
    public void pack() {
        CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.OBJECT).addFile(new File(id, encode()));
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
    public int[] getTransmogrifiedIds() {
        return this.transformedIds;
    }

    public void setOption(final int index, final String option) {
        if (options == null) {
            options = new String[5];
        }
        options[index] = option.isEmpty() ? null : option;
    }

    public ObjectDefinitions(int id, String name, int varbit, int optionsInvisible, int[] models, int[] types, int[] transformedIds, int ambientSoundId, int varp, int supportItems, int[] anIntArray100, int mapIconId, int sizeX, int clipType, boolean isRotated, int sizeY, boolean projectileClip, int anInt455, boolean nonFlatShading, int contouredGround, int anInt456, boolean modelClipped, int ambient, String[] options, int contrast, int anInt457, boolean hollow, int animationId, int modelSizeX, int decorDisplacement, int modelSizeHeight, int modelSizeY, int[] modelColours, boolean clipped, short[] modelTexture, int mapSceneId, int[] replacementColours, int offsetX, short[] replacementTexture, int offsetHeight, int offsetY, boolean obstructsGround, int accessBlockFlag, int finalTransformation, Int2ObjectMap<Object> parameters) {
        this.id = id;
        this.name = name;
        this.varbit = varbit;
        this.optionsInvisible = optionsInvisible;
        this.models = models;
        this.types = types;
        this.transformedIds = transformedIds;
        this.ambientSoundId = ambientSoundId;
        this.varp = varp;
        this.supportItems = supportItems;
        this.anIntArray100 = anIntArray100;
        this.mapIconId = mapIconId;
        this.sizeX = sizeX;
        this.clipType = clipType;
        this.isRotated = isRotated;
        this.sizeY = sizeY;
        this.projectileClip = projectileClip;
        this.ambientSoundDistance = anInt455;
        this.nonFlatShading = nonFlatShading;
        this.contouredGround = contouredGround;
        this.anInt456 = anInt456;
        this.modelClipped = modelClipped;
        this.ambient = ambient;
        this.options = options;
        this.contrast = contrast;
        this.anInt457 = anInt457;
        this.hollow = hollow;
        this.animationId = animationId;
        this.modelSizeX = modelSizeX;
        this.decorDisplacement = decorDisplacement;
        this.modelSizeHeight = modelSizeHeight;
        this.modelSizeY = modelSizeY;
        this.modelColours = modelColours;
        this.clipped = clipped;
        this.modelTexture = modelTexture;
        this.mapSceneId = mapSceneId;
        this.replacementColours = replacementColours;
        this.offsetX = offsetX;
        this.replacementTexture = replacementTexture;
        this.offsetHeight = offsetHeight;
        this.offsetY = offsetY;
        this.obstructsGround = obstructsGround;
        this.accessBlockFlag = accessBlockFlag;
        this.finalTransformation = finalTransformation;
        this.parameters = parameters;
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

    public int getVarbit() {
        return varbit;
    }

    public void setVarbit(int varbit) {
        this.varbit = varbit;
    }

    public int getOptionsInvisible() {
        return optionsInvisible;
    }

    public void setOptionsInvisible(int optionsInvisible) {
        this.optionsInvisible = optionsInvisible;
    }

    public int[] getModels() {
        return models;
    }

    public void setModels(int[] models) {
        this.models = models;
    }

    public int[] getTypes() {
        return types;
    }

    public void setTypes(int[] types) {
        this.types = types;
    }

    public int[] getTransformedIds() {
        return transformedIds;
    }

    public void setTransformedIds(int[] transformedIds) {
        this.transformedIds = transformedIds;
    }

    public int getAmbientSoundId() {
        return ambientSoundId;
    }

    public void setAmbientSoundId(int ambientSoundId) {
        this.ambientSoundId = ambientSoundId;
    }

    public int getVarp() {
        return varp;
    }

    public int getSupportItems() {
        return supportItems;
    }

    public int[] getAnIntArray100() {
        return anIntArray100;
    }

    public int getMapIconId() {
        return mapIconId;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getClipType() {
        return clipType;
    }

    public void setClipType(int clipType) {
        this.clipType = clipType;
    }

    public boolean isRotated() {
        return isRotated;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public boolean isProjectileClip() {
        return projectileClip;
    }

    public void setProjectileClip(boolean projectileClip) {
        this.projectileClip = projectileClip;
    }

    public int getAnInt455() {
        return ambientSoundDistance;
    }

    public void setAnInt455(int anInt455) {
        this.ambientSoundDistance = anInt455;
    }

    public boolean isNonFlatShading() {
        return nonFlatShading;
    }

    public int getContouredGround() {
        return contouredGround;
    }

    public void setContouredGround(int contouredGround) {
        this.contouredGround = contouredGround;
    }

    public int getAnInt456() {
        return anInt456;
    }

    public boolean isModelClipped() {
        return modelClipped;
    }

    public int getAmbient() {
        return ambient;
    }

    public void setAmbient(int ambient) {
        this.ambient = ambient;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public int getAnInt457() {
        return anInt457;
    }

    public boolean isHollow() {
        return hollow;
    }

    public int getAnimationId() {
        return animationId;
    }

    public void setAnimationId(int animationId) {
        this.animationId = animationId;
    }

    public int getModelSizeX() {
        return modelSizeX;
    }

    public void setModelSizeX(int modelSizeX) {
        this.modelSizeX = modelSizeX;
    }

    public int getDecorDisplacement() {
        return decorDisplacement;
    }

    public int getModelSizeHeight() {
        return modelSizeHeight;
    }

    public void setModelSizeHeight(int modelSizeHeight) {
        this.modelSizeHeight = modelSizeHeight;
    }

    public int getModelSizeY() {
        return modelSizeY;
    }

    public void setModelSizeY(int modelSizeY) {
        this.modelSizeY = modelSizeY;
    }

    public int[] getModelColours() {
        return modelColours;
    }

    public void setModelColours(int[] modelColours) {
        this.modelColours = modelColours;
    }

    public boolean isClipped() {
        return clipped;
    }

    public short[] getModelTexture() {
        return modelTexture;
    }

    public void setModelTexture(short[] var) {
        this.modelTexture = var;
    }

    public void setReplacementTexture(short[] var) {
        this.replacementTexture = var;
    }

    public int getMapSceneId() {
        return mapSceneId;
    }

    public void setMapSceneId(int mapSceneId) {
        this.mapSceneId = mapSceneId;
    }

    public int[] getReplacementColours() {
        return replacementColours;
    }

    public void setReplacementColours(int[] replacementColours) {
        this.replacementColours = replacementColours;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public short[] getReplacementTexture() {
        return replacementTexture;
    }

    public int getOffsetHeight() {
        return offsetHeight;
    }

    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isObstructsGround() {
        return obstructsGround;
    }

    public int getAccessBlockFlag() {
        return accessBlockFlag;
    }

    public void setAccessBlockFlag(int accessBlockFlag) {
        this.accessBlockFlag = accessBlockFlag;
    }

    public int getFinalTransformation() {
        return finalTransformation;
    }

    public void setFinalTransformation(int finalTransformation) {
        this.finalTransformation = finalTransformation;
    }

    public Int2ObjectMap<Object> getParameters() {
        return parameters;
    }

    public void setParameters(Int2ObjectMap<Object> parameters) {
        this.parameters = parameters;
    }


    public static class ObjectDefinitionsBuilder {
        private int id;
        private String name;
        private int varbit;
        private int optionsInvisible;
        private int[] models;
        private int[] types;
        private int[] transformedIds;
        private int ambientSoundId;
        private int varp;
        private int supportItems;
        private int[] anIntArray100;
        private int mapIconId;
        private int sizeX;
        private int clipType;
        private boolean isRotated;
        private int sizeY;
        private boolean projectileClip;
        private int anInt455;
        private boolean nonFlatShading;
        private int contouredGround;
        private int anInt456;
        private boolean modelClipped;
        private int ambient;
        private String[] options;
        private int contrast;
        private int anInt457;
        private boolean hollow;
        private int animationId;
        private int modelSizeX;
        private int decorDisplacement;
        private int modelSizeHeight;
        private int modelSizeY;
        private int[] modelColours;
        private boolean clipped;
        private short[] modelTexture;
        private int mapSceneId;
        private int[] replacementColours;
        private int offsetX;
        private short[] replacementTexture;
        private int offsetHeight;
        private int offsetY;
        private boolean obstructsGround;
        private int accessBlockFlag;
        private int finalTransformation;
        private Int2ObjectMap<Object> parameters;

        ObjectDefinitionsBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder id(final int id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder varbit(final int varbit) {
            this.varbit = varbit;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder optionsInvisible(final int optionsInvisible) {
            this.optionsInvisible = optionsInvisible;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder models(final int[] models) {
            this.models = models;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder types(final int[] types) {
            this.types = types;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder transformedIds(final int[] transformedIds) {
            this.transformedIds = transformedIds;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder ambientSoundId(final int ambientSoundId) {
            this.ambientSoundId = ambientSoundId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder varp(final int varp) {
            this.varp = varp;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder supportItems(final int supportItems) {
            this.supportItems = supportItems;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder anIntArray100(final int[] anIntArray100) {
            this.anIntArray100 = anIntArray100;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder mapIconId(final int mapIconId) {
            this.mapIconId = mapIconId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder sizeX(final int sizeX) {
            this.sizeX = sizeX;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder clipType(final int clipType) {
            this.clipType = clipType;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder isRotated(final boolean isRotated) {
            this.isRotated = isRotated;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder sizeY(final int sizeY) {
            this.sizeY = sizeY;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder projectileClip(final boolean projectileClip) {
            this.projectileClip = projectileClip;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder anInt455(final int anInt455) {
            this.anInt455 = anInt455;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder nonFlatShading(final boolean nonFlatShading) {
            this.nonFlatShading = nonFlatShading;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder contouredGround(final int contouredGround) {
            this.contouredGround = contouredGround;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder anInt456(final int anInt456) {
            this.anInt456 = anInt456;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder modelClipped(final boolean modelClipped) {
            this.modelClipped = modelClipped;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder ambient(final int ambient) {
            this.ambient = ambient;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder options(final String[] options) {
            this.options = options;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder contrast(final int contrast) {
            this.contrast = contrast;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder anInt457(final int anInt457) {
            this.anInt457 = anInt457;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder hollow(final boolean hollow) {
            this.hollow = hollow;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder animationId(final int animationId) {
            this.animationId = animationId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder modelSizeX(final int modelSizeX) {
            this.modelSizeX = modelSizeX;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder decorDisplacement(final int decorDisplacement) {
            this.decorDisplacement = decorDisplacement;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder modelSizeHeight(final int modelSizeHeight) {
            this.modelSizeHeight = modelSizeHeight;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder modelSizeY(final int modelSizeY) {
            this.modelSizeY = modelSizeY;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder modelColours(final int[] modelColours) {
            this.modelColours = modelColours;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder clipped(final boolean clipped) {
            this.clipped = clipped;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder modelTexture(final short[] modelTexture) {
            this.modelTexture = modelTexture;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder mapSceneId(final int mapSceneId) {
            this.mapSceneId = mapSceneId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder replacementColours(final int[] replacementColours) {
            this.replacementColours = replacementColours;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder offsetX(final int offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder replacementTexture(final short[] replacementTexture) {
            this.replacementTexture = replacementTexture;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder offsetHeight(final int offsetHeight) {
            this.offsetHeight = offsetHeight;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder offsetY(final int offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder obstructsGround(final boolean obstructsGround) {
            this.obstructsGround = obstructsGround;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder accessBlockFlag(final int accessBlockFlag) {
            this.accessBlockFlag = accessBlockFlag;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder finalTransformation(final int finalTransformation) {
            this.finalTransformation = finalTransformation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ObjectDefinitions.ObjectDefinitionsBuilder parameters(final Int2ObjectMap<Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public ObjectDefinitions build() {
            return new ObjectDefinitions(this.id, this.name, this.varbit, this.optionsInvisible, this.models, this.types, this.transformedIds, this.ambientSoundId, this.varp, this.supportItems, this.anIntArray100, this.mapIconId, this.sizeX, this.clipType, this.isRotated, this.sizeY, this.projectileClip, this.anInt455, this.nonFlatShading, this.contouredGround, this.anInt456, this.modelClipped, this.ambient, this.options, this.contrast, this.anInt457, this.hollow, this.animationId, this.modelSizeX, this.decorDisplacement, this.modelSizeHeight, this.modelSizeY, this.modelColours, this.clipped, this.modelTexture, this.mapSceneId, this.replacementColours, this.offsetX, this.replacementTexture, this.offsetHeight, this.offsetY, this.obstructsGround, this.accessBlockFlag, this.finalTransformation, this.parameters);
        }

        @Override
        public String toString() {
            return "ObjectDefinitions.ObjectDefinitionsBuilder(id=" + this.id + ", name=" + this.name + ", varbit=" + this.varbit + ", optionsInvisible=" + this.optionsInvisible + ", models=" + Arrays.toString(this.models) + ", types=" + Arrays.toString(this.types) + ", transformedIds=" + Arrays.toString(this.transformedIds) + ", ambientSoundId=" + this.ambientSoundId + ", varp=" + this.varp + ", supportItems=" + this.supportItems + ", anIntArray100=" + Arrays.toString(this.anIntArray100) + ", mapIconId=" + this.mapIconId + ", sizeX=" + this.sizeX + ", clipType=" + this.clipType + ", isRotated=" + this.isRotated + ", sizeY=" + this.sizeY + ", projectileClip=" + this.projectileClip + ", anInt455=" + this.anInt455 + ", nonFlatShading=" + this.nonFlatShading + ", contouredGround=" + this.contouredGround + ", anInt456=" + this.anInt456 + ", modelClipped=" + this.modelClipped + ", ambient=" + this.ambient + ", options=" + Arrays.deepToString(this.options) + ", contrast=" + this.contrast + ", anInt457=" + this.anInt457 + ", hollow=" + this.hollow + ", animationId=" + this.animationId + ", modelSizeX=" + this.modelSizeX + ", decorDisplacement=" + this.decorDisplacement + ", modelSizeHeight=" + this.modelSizeHeight + ", modelSizeY=" + this.modelSizeY + ", modelColours=" + Arrays.toString(this.modelColours) + ", clipped=" + this.clipped + ", modelTexture=" + Arrays.toString(this.modelTexture) + ", mapSceneId=" + this.mapSceneId + ", replacementColours=" + Arrays.toString(this.replacementColours) + ", offsetX=" + this.offsetX + ", replacementTexture=" + Arrays.toString(this.replacementTexture) + ", offsetHeight=" + this.offsetHeight + ", offsetY=" + this.offsetY + ", obstructsGround=" + this.obstructsGround + ", accessBlockFlag=" + this.accessBlockFlag + ", finalTransformation=" + this.finalTransformation + ", parameters=" + this.parameters + ")";
        }
    }

    public static ObjectDefinitions.ObjectDefinitionsBuilder builder() {
        return new ObjectDefinitions.ObjectDefinitionsBuilder();
    }

    public ObjectDefinitions.ObjectDefinitionsBuilder toBuilder() {
        return new ObjectDefinitions.ObjectDefinitionsBuilder().id(this.id).name(this.name).varbit(this.varbit).optionsInvisible(this.optionsInvisible).models(this.models).types(this.types).transformedIds(this.transformedIds).ambientSoundId(this.ambientSoundId).varp(this.varp).supportItems(this.supportItems).anIntArray100(this.anIntArray100).mapIconId(this.mapIconId).sizeX(this.sizeX).clipType(this.clipType).isRotated(this.isRotated).sizeY(this.sizeY).projectileClip(this.projectileClip).anInt455(this.ambientSoundDistance).nonFlatShading(this.nonFlatShading).contouredGround(this.contouredGround).anInt456(this.anInt456).modelClipped(this.modelClipped).ambient(this.ambient).options(this.options).contrast(this.contrast).anInt457(this.anInt457).hollow(this.hollow).animationId(this.animationId).modelSizeX(this.modelSizeX).decorDisplacement(this.decorDisplacement).modelSizeHeight(this.modelSizeHeight).modelSizeY(this.modelSizeY).modelColours(this.modelColours).clipped(this.clipped).modelTexture(this.modelTexture).mapSceneId(this.mapSceneId).replacementColours(this.replacementColours).offsetX(this.offsetX).replacementTexture(this.replacementTexture).offsetHeight(this.offsetHeight).offsetY(this.offsetY).obstructsGround(this.obstructsGround).accessBlockFlag(this.accessBlockFlag).finalTransformation(this.finalTransformation).parameters(this.parameters);
    }

    @Override
    public String toString() {
        return "ObjectDefinitions(id=" + this.getId() + ", name=" + this.getName() + ", varbit=" + this.getVarbit() + ", optionsInvisible=" + this.getOptionsInvisible() + ", models=" + Arrays.toString(this.getModels()) + ", types=" + Arrays.toString(this.getTypes()) + ", transformedIds=" + Arrays.toString(this.getTransformedIds()) + ", ambientSoundId=" + this.getAmbientSoundId() + ", varp=" + this.getVarp() + ", supportItems=" + this.getSupportItems() + ", anIntArray100=" + Arrays.toString(this.getAnIntArray100()) + ", mapIconId=" + this.getMapIconId() + ", sizeX=" + this.getSizeX() + ", clipType=" + this.getClipType() + ", isRotated=" + this.isRotated() + ", sizeY=" + this.getSizeY() + ", projectileClip=" + this.isProjectileClip() + ", anInt455=" + this.getAnInt455() + ", nonFlatShading=" + this.isNonFlatShading() + ", contouredGround=" + this.getContouredGround() + ", anInt456=" + this.getAnInt456() + ", modelClipped=" + this.isModelClipped() + ", ambient=" + this.getAmbient() + ", options=" + Arrays.deepToString(this.getOptions()) + ", contrast=" + this.getContrast() + ", anInt457=" + this.getAnInt457() + ", hollow=" + this.isHollow() + ", animationId=" + this.getAnimationId() + ", modelSizeX=" + this.getModelSizeX() + ", decorDisplacement=" + this.getDecorDisplacement() + ", modelSizeHeight=" + this.getModelSizeHeight() + ", modelSizeY=" + this.getModelSizeY() + ", modelColours=" + Arrays.toString(this.getModelColours()) + ", clipped=" + this.isClipped() + ", modelTexture=" + Arrays.toString(this.getModelTexture()) + ", mapSceneId=" + this.getMapSceneId() + ", replacementColours=" + Arrays.toString(this.getReplacementColours()) + ", offsetX=" + this.getOffsetX() + ", replacementTexture=" + Arrays.toString(this.getReplacementTexture()) + ", offsetHeight=" + this.getOffsetHeight() + ", offsetY=" + this.getOffsetY() + ", obstructsGround=" + this.isObstructsGround() + ", accessBlockFlag=" + this.getAccessBlockFlag() + ", finalTransformation=" + this.getFinalTransformation() + ", parameters=" + this.getParameters() + ")";
    }
}
