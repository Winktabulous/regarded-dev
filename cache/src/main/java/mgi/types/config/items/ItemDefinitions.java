package mgi.types.config.items;

import com.zenyte.CacheManager;
import com.zenyte.game.content.grandexchange.JSONGEItemDefinitions;
import com.zenyte.game.content.grandexchange.JSONGEItemDefinitionsLoader;
import com.zenyte.game.parser.impl.ItemRequirements;
import com.zenyte.game.parser.impl.JSONItemDefinitionsLoader;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType;
import com.zenyte.plugins.PluginManager;
import com.zenyte.plugins.events.ItemDefinitionsLoadedEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author Kris | 22. jaan 2018 : 21:35.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ItemDefinitions implements Definitions, Cloneable {

    private static final Logger log = LoggerFactory.getLogger(ItemDefinitions.class);

    public static final ItemDefinitions DEFAULT = new ItemDefinitions(-1);

    private static final IntOpenHashSet packedIDs = new IntOpenHashSet();

    private static ItemDefinitions[] definitions;

    public static int getSellPrice(final int itemId) {
        final ItemDefinitions definitions = ItemDefinitions.get(itemId);
        if (definitions == null) {
            return 0;
        }
        final boolean noted = definitions.isNoted();
        final int id = noted ? definitions.getNotedId() : itemId;
        final JSONGEItemDefinitions gePrice = JSONGEItemDefinitionsLoader.lookup(id);
        return gePrice != null && gePrice.getPrice() != 0 ? gePrice.getPrice() : definitions.getPrice();
    }

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        load(cache);

        PluginManager.post(new ItemDefinitionsLoadedEvent(this));
    }

    public void load(Cache cache) {
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group items = configs.findGroupByID(GroupType.ITEM);
        definitions = new ItemDefinitions[65535/*items.getHighestFileId()*/];
        for (int id = 0; id < items.getHighestFileId(); id++) {
            final File file = items.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            definitions[id] = new ItemDefinitions(id, buffer);
        }
        for (int id = 0; id < items.getHighestFileId(); id++) {
            final ItemDefinitions defs = get(id);
            if (defs == null || defs.notedTemplate == -1) {
                continue;
            }
            defs.toNote();
        }
    }

    public static boolean isValid(final int id) {
        return id >= 0 && id < definitions.length;
    }

    public static boolean isInvalid(final int id) {
        return id < 0 || id >= definitions.length;
    }

    String name;
    transient String lowercaseName;
    int id;
    String[] inventoryOptions;
    String[] groundOptions;
    boolean grandExchange;
    boolean isMembers;
    int isStackable;
    int price;
    int notedTemplate;
    int notedId;
    int bindTemplateId;
    int bindId;
    int placeholderTemplate;
    int placeholderId;
    int[] stackIds;
    int[] stackAmounts;
    int maleOffset;
    int primaryMaleHeadModelId;
    int secondaryMaleHeadModelId;
    int primaryMaleModel;
    int secondaryMaleModel;
    int tertiaryMaleModel;
    int femaleOffset;
    int primaryFemaleHeadModelId;
    int secondaryFemaleHeadModelId;
    int primaryFemaleModel;
    int secondaryFemaleModel;
    int tertiaryFemaleModel;
    int inventoryModelId;
    int shiftClickIndex;
    int teamId;
    int zoom;
    int offsetX;
    int offsetY;
    int modelPitch;
    int modelRoll;
    int modelYaw;
    int resizeX;
    int resizeY;
    int resizeZ;
    short[] originalColours;
    short[] replacementColours;
    short[] originalTextureIds;
    short[] replacementTextureIds;
    int ambient;
    int contrast;
    int wearPos1;
    int wearPos2;
    int wearPos3;
    int cacheWeight;
    int category;
    Int2ObjectMap<Object> parameters;
    private String examine;
    private float weight;
    private int slot = -1;
    // @Getter
    // private HashMap<Integer, Integer> requirements;
    private int[] bonuses;
    private EquipmentType equipmentType;
    private boolean twoHanded;
    private int blockAnimation;
    private int standAnimation = RenderAnimation.STAND;
    private int walkAnimation = RenderAnimation.WALK;
    private int runAnimation = RenderAnimation.RUN;
    private int standTurnAnimation = RenderAnimation.STAND_TURN;
    private int rotate90Animation = RenderAnimation.ROTATE90;
    private int rotate180Animation = RenderAnimation.ROTATE180;
    private int rotate270Animation = RenderAnimation.ROTATE270;
    private int accurateAnimation;
    private int aggressiveAnimation;
    private int controlledAnimation;
    private int defensiveAnimation;
    private int attackSpeed;
    private int interfaceVarbit;
    private int normalAttackDistance;
    private int longAttackDistance;

    public int getNotedOrDefault() {
        if (isNoted() || notedId == -1) {
            return id;
        }
        return notedId;
    }

    public int getUnnotedOrDefault() {
        if (isNoted()) {
            return notedId;
        }
        return id;
    }

    public ItemRequirements.ItemRequirement getRequirements() {
        return ItemRequirements.getRequirement(id);
    }

    public boolean containsOption(final String option) {
        if (inventoryOptions == null) {
            return false;
        }
        for (final String o : inventoryOptions) {
            if (o == null || !o.equalsIgnoreCase(option)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public int getSlotForOption(final String option) {
        if (inventoryOptions == null) {
            return -1;
        }
        for (int i = 0; i < inventoryOptions.length; i++) {
            final String o = inventoryOptions[i];
            if (o == null || !o.equalsIgnoreCase(option)) {
                continue;
            }
            return i + 1;
        }
        return -1;
    }

    public String getOption(final int option) {
        if (inventoryOptions == null) {
            return null;
        }
        if (option < 0 || option >= inventoryOptions.length) {
            return null;
        }
        return inventoryOptions[option];
    }

    private static boolean loaded;

    public static void loadDefinitions(ExecutorService service, Runnable withParsed, Runnable afterParsed) {
        if (loaded) {
            return;
        }
        loaded = true;
        try {
            service.invokeAll(List.of(
                    () -> {
                        try {
                            withParsed.run();
                        } catch (Throwable throwable) {
                            log.error("", throwable);
                        }
                        return null;
                    },
                    () -> {
                        try {
                            new JSONItemDefinitionsLoader().parse();
                        } catch (Throwable throwable) {
                            log.error("", throwable);
                        }
                        return null;
                    },
                    () -> {
                        try {
                            ItemRequirements.parse();
                        } catch (Throwable throwable) {
                            log.error("", throwable);
                        }
                        return null;
                    }));
        } catch (InterruptedException e) {
            log.error("", e);
            return;
        }
        try {
            for (JSONItemDefinitions jsonDefs : JSONItemDefinitionsLoader.getDefinitions().values()) {
                int itemID = jsonDefs.getId();
                ItemDefinitions def = ItemDefinitions.get(itemID);
                if (def == null) {
                    log.warn("JSON item def was defined for " + itemID + " but definition was null");
                    continue;
                }
                apply(def, jsonDefs);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
        afterParsed.run();
    }

    public static void apply(JSONItemDefinitions jsonDefs) {
        var id = jsonDefs.getId();
        var definition = ItemDefinitions.get(id);
        if (definition == null) {
            System.err.println("Unable to find item definition for id " + id);
            return;
        }
        apply(definition, jsonDefs);
    }

    private static void apply(ItemDefinitions def, JSONItemDefinitions jsonDefs) {
        final WearableDefinition wearDef = jsonDefs.getEquipmentDefinition();
        def.weight = jsonDefs.getWeight();
        def.slot = jsonDefs.getSlot();
        if (jsonDefs.getExamine() != null && !jsonDefs.getExamine().isEmpty()) {
            def.examine = jsonDefs.getExamine();
        }
        // if (jsonDefs.getTradable() != null) {
        // def.tradable = jsonDefs.getTradable();
        // }
        def.equipmentType = jsonDefs.getEquipmentType();
        if (wearDef != null) {
            // def.requirements = wearDef.getRequirements();
            final String bonuses = wearDef.getBonuses();
            if (bonuses != null && !bonuses.isBlank()) {
                final String[] splitBonuses = bonuses.split(",");
                try {
                    def.bonuses = new int[splitBonuses.length];
                    for (int i = 0; i < splitBonuses.length; i++) {
                        def.bonuses[i] = Integer.parseInt(splitBonuses[i].trim());
                    }
                } catch (final Exception e) {
                    log.error("Failed to read item bonuses for {}", def, e);
                }
            } else
                def.setBonuses(new int[14]);
            final WieldableDefinition wieldDef = wearDef.getWeaponDefinition();
            if (wieldDef != null) {
                def.twoHanded = wieldDef.isTwoHanded();
                if (wieldDef.getBlockAnimation() != 0) {
                    def.blockAnimation = wieldDef.getBlockAnimation();
                }
                if (wieldDef.getStandAnimation() != 0) {
                    def.standAnimation = wieldDef.getStandAnimation();
                }
                if (wieldDef.getWalkAnimation() != 0) {
                    def.walkAnimation = wieldDef.getWalkAnimation();
                }
                if (wieldDef.getRunAnimation() != 0) {
                    def.runAnimation = wieldDef.getRunAnimation();
                }
                if (wieldDef.getStandTurnAnimation() != 0) {
                    def.standTurnAnimation = wieldDef.getStandTurnAnimation();
                }
                if (wieldDef.getRotate90Animation() != 0) {
                    def.rotate90Animation = wieldDef.getRotate90Animation();
                }
                if (wieldDef.getRotate180Animation() != 0) {
                    def.rotate180Animation = wieldDef.getRotate180Animation();
                }
                if (wieldDef.getRotate270Animation() != 0) {
                    def.rotate270Animation = wieldDef.getRotate270Animation();
                }
                def.accurateAnimation = wieldDef.getAccurateAnimation();
                def.aggressiveAnimation = wieldDef.getAggressiveAnimation();
                def.controlledAnimation = wieldDef.getControlledAnimation();
                def.defensiveAnimation = wieldDef.getDefensiveAnimation();
                def.attackSpeed = wieldDef.getAttackSpeed();
                def.interfaceVarbit = wieldDef.getInterfaceVarbit();
                def.normalAttackDistance = wieldDef.getNormalAttackDistance();
                def.longAttackDistance = wieldDef.getLongAttackDistance();
            }
            final Map<Integer, Integer> skillLevelRequirements = wearDef.getRequirements();
            if (skillLevelRequirements != null)
                skillLevelRequirements.forEach((skill, level) -> ItemRequirements.add(def.id, skill, level));
        }
    }

    public ItemDefinitions(final int id) {
        this.id = id;
        setDefaults();
    }

    public ItemDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        setDefaults();
        decode(buffer);
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
        ItemDefinitionsDecoding.decodeOpcode(this, buffer, opcode);
    }

    private void setDefaults() {
        name = lowercaseName = "null";
        zoom = 2000;
        modelPitch = 0;
        modelRoll = 0;
        modelYaw = 0;
        offsetX = 0;
        offsetY = 0;
        isStackable = 0;
        price = 1;
        isMembers = false;
        groundOptions = new String[]{null, null, "Take", null, null};
        inventoryOptions = new String[]{null, null, null, null, "Drop"};
        shiftClickIndex = -2;
        primaryMaleModel = -1;
        secondaryMaleModel = -1;
        maleOffset = 0;
        primaryFemaleModel = -1;
        secondaryFemaleModel = -1;
        femaleOffset = 0;
        tertiaryMaleModel = -1;
        tertiaryFemaleModel = -1;
        primaryMaleHeadModelId = -1;
        secondaryMaleHeadModelId = -1;
        primaryFemaleHeadModelId = -1;
        secondaryFemaleHeadModelId = -1;
        notedId = -1;
        notedTemplate = -1;
        resizeX = 128;
        resizeY = 128;
        resizeZ = 128;
        ambient = 0;
        contrast = 0;
        teamId = 0;
        grandExchange = false;
        bindId = -1;
        bindTemplateId = -1;
        placeholderId = -1;
        placeholderTemplate = -1;
    }

    public static ItemDefinitions get(final int id) {
        if (id < 0 || id >= definitions.length) {
            // throw new RuntimeException("Invalid item requested.");
            return null;// cant throw an exception because so much code is unable to handle exceptions.
        }
        return definitions[id];
    }

    public static ItemDefinitions getOrThrow(final int id) {
        if (id < 0 || id >= definitions.length) {
            throw new RuntimeException("Invalid item requested.");
        }
        return definitions[id];
    }

    public static String nameOf(final int id) {
        final ItemDefinitions def = get(id);
        return def == null ? "null" : def.getName();
    }

    public static String nameOfLowercase(final int id) {
        final ItemDefinitions def = get(id);
        return def == null ? "null" : def.getLowercaseName();
    }

    public boolean isPlaceholder() {
        return placeholderTemplate != -1;
    }

    public boolean isNoted() {
        return notedTemplate != -1;
    }

    public boolean isStackable() {
        return isStackable == 1 || isNoted();
    }

    public int getPrice() {
        if (isNoted()) {
            return get(getNotedId()).getPrice();
        }
        return price;
    }

    private void toNote() {
        final ItemDefinitions realItem = get(notedId);
        if (realItem == null) {
            System.err.println("TF? " + id + " / noted " + notedId);
            return;
        }
        isMembers = realItem.isMembers;
        price = realItem.price;
        name = realItem.name;
        grandExchange = realItem.grandExchange;
        isStackable = 1;
    }

    public int getHighAlchPrice() {
        return (int) (getPrice() * 0.6);
    }

    public String getStringParam(final int key) {
        if (parameters == null) {
            return "null";
        }
        final Object object = parameters.get(key);
        if (!(object instanceof String)) {
            return "null";
        }
        return (String) object;
    }

    public int getIntParam(final int key) {
        if (parameters == null) {
            return -1;
        }
        final Object object = parameters.get(key);
        if (!(object instanceof Integer)) {
            return -1;
        }
        return (Integer) object;
    }

    public boolean containsParamByValue(final Object value) {
        if (parameters == null) {
            return false;
        }
        final ObjectIterator<Object> iterator = parameters.values().iterator();
        final String lowercaseValue = value.toString().toLowerCase();
        while (iterator.hasNext()) {
            final Object next = iterator.next();
            if (next.toString().toLowerCase().equals(lowercaseValue)) {
                return true;
            }
        }
        return false;
    }

    public ItemDefinitions(String name, String lowercaseName, int id, String[] inventoryOptions,
                           String[] groundOptions, boolean grandExchange, boolean isMembers, int isStackable,
                           int price, int notedTemplate, int notedId, int bindTemplateId, int bindId,
                           int placeholderTemplate, int placeholderId, int[] stackIds, int[] stackAmounts,
                           int maleOffset, int primaryMaleHeadModelId, int secondaryMaleHeadModelId,
                           int primaryMaleModel, int secondaryMaleModel, int tertiaryMaleModel, int femaleOffset,
                           int primaryFemaleHeadModelId, int secondaryFemaleHeadModelId, int primaryFemaleModel,
                           int secondaryFemaleModel, int tertiaryFemaleModel, int inventoryModelId,
                           int shiftClickIndex, int teamId, int zoom, int offsetX, int offsetY, int modelPitch,
                           int modelRoll, int modelYaw, int resizeX, int resizeY, int resizeZ,
                           short[] originalColours, short[] replacementColours, short[] originalTextureIds,
                           short[] replacementTextureIds, int ambient, int contrast,
                           Int2ObjectMap<Object> parameters, String examine, float weight, int slot,
                           int[] bonuses, EquipmentType equipmentType, boolean twoHanded, int blockAnimation,
                           int standAnimation, int walkAnimation, int runAnimation, int standTurnAnimation,
                           int rotate90Animation, int rotate180Animation, int rotate270Animation,
                           int accurateAnimation, int aggressiveAnimation, int controlledAnimation,
                           int defensiveAnimation, int attackSpeed, int interfaceVarbit, int normalAttackDistance,
                           int longAttackDistance) {
        this.name = name;
        this.lowercaseName = lowercaseName;
        this.id = id;
        this.inventoryOptions = inventoryOptions;
        this.groundOptions = groundOptions;
        this.grandExchange = grandExchange;
        this.isMembers = isMembers;
        this.isStackable = isStackable;
        this.price = price;
        this.notedTemplate = notedTemplate;
        this.notedId = notedId;
        this.bindTemplateId = bindTemplateId;
        this.bindId = bindId;
        this.placeholderTemplate = placeholderTemplate;
        this.placeholderId = placeholderId;
        this.stackIds = stackIds;
        this.stackAmounts = stackAmounts;
        this.maleOffset = maleOffset;
        this.primaryMaleHeadModelId = primaryMaleHeadModelId;
        this.secondaryMaleHeadModelId = secondaryMaleHeadModelId;
        this.primaryMaleModel = primaryMaleModel;
        this.secondaryMaleModel = secondaryMaleModel;
        this.tertiaryMaleModel = tertiaryMaleModel;
        this.femaleOffset = femaleOffset;
        this.primaryFemaleHeadModelId = primaryFemaleHeadModelId;
        this.secondaryFemaleHeadModelId = secondaryFemaleHeadModelId;
        this.primaryFemaleModel = primaryFemaleModel;
        this.secondaryFemaleModel = secondaryFemaleModel;
        this.tertiaryFemaleModel = tertiaryFemaleModel;
        this.inventoryModelId = inventoryModelId;
        this.shiftClickIndex = shiftClickIndex;
        this.teamId = teamId;
        this.zoom = zoom;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.modelPitch = modelPitch;
        this.modelRoll = modelRoll;
        this.modelYaw = modelYaw;
        this.resizeX = resizeX;
        this.resizeY = resizeY;
        this.resizeZ = resizeZ;
        this.originalColours = originalColours;
        this.replacementColours = replacementColours;
        this.originalTextureIds = originalTextureIds;
        this.replacementTextureIds = replacementTextureIds;
        this.ambient = ambient;
        this.contrast = contrast;
        this.parameters = parameters;
        this.examine = examine;
        this.weight = weight;
        this.slot = slot;
        this.bonuses = bonuses;
        this.equipmentType = equipmentType;
        this.twoHanded = twoHanded;
        this.blockAnimation = blockAnimation;
        this.standAnimation = standAnimation;
        this.walkAnimation = walkAnimation;
        this.runAnimation = runAnimation;
        this.standTurnAnimation = standTurnAnimation;
        this.rotate90Animation = rotate90Animation;
        this.rotate180Animation = rotate180Animation;
        this.rotate270Animation = rotate270Animation;
        this.accurateAnimation = accurateAnimation;
        this.aggressiveAnimation = aggressiveAnimation;
        this.controlledAnimation = controlledAnimation;
        this.defensiveAnimation = defensiveAnimation;
        this.attackSpeed = attackSpeed;
        this.interfaceVarbit = interfaceVarbit;
        this.normalAttackDistance = normalAttackDistance;
        this.longAttackDistance = longAttackDistance;
    }

    public ItemDefinitions() {
    }

    public static IntOpenHashSet getPackedIDs() {
        return packedIDs;
    }

    @Override
    public void pack() {
        if (!packedIDs.add(id)) {
            log.info("Overlapping an item in cachepacking: " + id);
        }
        CacheManager.getCache()
                .getArchive(ArchiveType.CONFIGS)
                .findGroupByID(GroupType.ITEM)
                .addFile(new File(id, encode()));
    }

    public void setOption(final int index, final String option) {
        if (inventoryOptions == null) {
            inventoryOptions = new String[5];
        }
        inventoryOptions[index] = option.isEmpty() ? null : option;
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(512);
        buffer.writeByte(1);
        buffer.writeShort(inventoryModelId);
        if (!name.equals("null") && notedTemplate == -1) {
            buffer.writeByte(2);
            buffer.writeString(name);
        }
        if (zoom != 2000) {
            buffer.writeByte(4);
            buffer.writeShort(zoom);
        }
        if (modelPitch != 0) {
            buffer.writeByte(5);
            buffer.writeShort(modelPitch);
        }
        if (modelRoll != 0) {
            buffer.writeByte(6);
            buffer.writeShort(modelRoll);
        }
        if (offsetX != 0) {
            buffer.writeByte(7);
            buffer.writeShort(offsetX);
        }
        if (offsetY != 0) {
            buffer.writeByte(8);
            buffer.writeShort(offsetY);
        }
        if (isStackable == 1 && notedTemplate == -1) {
            buffer.writeByte(11);
        }
        if (price != 1 && notedTemplate == -1) {
            buffer.writeByte(12);
            buffer.writeInt(price);
        }
        if (wearPos1 != 0) {
            buffer.writeByte(13);
            buffer.writeByte(wearPos1);
        }
        if (wearPos2 != 0) {
            buffer.writeByte(14);
            buffer.writeByte(wearPos2);
        }
        if (isMembers && notedTemplate == -1) {
            buffer.writeByte(16);
        }
        if (primaryMaleModel != -1) {
            buffer.writeByte(23);
            buffer.writeShort(primaryMaleModel);
            buffer.writeByte(maleOffset);
        }
        if (secondaryMaleModel != -1) {
            buffer.writeByte(24);
            buffer.writeShort(secondaryMaleModel);
        }
        if (primaryFemaleModel != -1) {
            buffer.writeByte(25);
            buffer.writeShort(primaryFemaleModel);
            buffer.writeByte(femaleOffset);
        }
        if (secondaryFemaleModel != -1) {
            buffer.writeByte(26);
            buffer.writeShort(secondaryFemaleModel);
        }
        if (wearPos3 != 0) {
            buffer.writeByte(27);
            buffer.writeByte(wearPos3);
        }
        if (groundOptions != null) {
            for (int index = 0; index < 5; index++) {
                if (groundOptions[index] != null) {
                    buffer.writeByte((30 + index));
                    buffer.writeString(groundOptions[index]);
                }
            }
        }
        if (inventoryOptions != null) {
            for (int index = 0; index < 5; index++) {
                if (inventoryOptions[index] != null) {
                    buffer.writeByte((35 + index));
                    buffer.writeString(inventoryOptions[index]);
                }
            }
        }
        if (originalColours != null && replacementColours != null && originalColours.length != 0
                && replacementColours.length != 0) {
            buffer.writeByte(40);
            buffer.writeByte(originalColours.length);
            for (int index = 0; index < originalColours.length; index++) {
                buffer.writeShort(originalColours[index]);
                buffer.writeShort(replacementColours[index]);
            }
        }
        if (originalTextureIds != null && replacementTextureIds != null
                && originalTextureIds.length != 0 && replacementTextureIds.length != 0) {
            buffer.writeByte(41);
            buffer.writeByte(originalTextureIds.length);
            for (int index = 0; index < originalTextureIds.length; index++) {
                buffer.writeShort(originalTextureIds[index]);
                buffer.writeShort(replacementTextureIds[index]);
            }
        }
        if (shiftClickIndex != -1) {
            buffer.writeByte(42);
            buffer.writeByte(shiftClickIndex);
        }
        if (grandExchange) {
            buffer.writeByte(65);
        }
        if (cacheWeight != 0) {
            buffer.writeByte(75);
            buffer.writeShort(cacheWeight);
        }
        if (tertiaryMaleModel != -1) {
            buffer.writeByte(78);
            buffer.writeShort(tertiaryMaleModel);
        }
        if (tertiaryFemaleModel != -1) {
            buffer.writeByte(79);
            buffer.writeShort(tertiaryFemaleModel);
        }
        if (primaryMaleHeadModelId != -1) {
            buffer.writeByte(90);
            buffer.writeShort(primaryMaleHeadModelId);
        }
        if (primaryFemaleHeadModelId != -1) {
            buffer.writeByte(91);
            buffer.writeShort(primaryFemaleHeadModelId);
        }
        if (secondaryMaleHeadModelId != -1) {
            buffer.writeByte(92);
            buffer.writeShort(secondaryMaleHeadModelId);
        }
        if (secondaryFemaleHeadModelId != -1) {
            buffer.writeByte(93);
            buffer.writeShort(secondaryFemaleHeadModelId);
        }
        if (category != 0) {
            buffer.writeByte(94);
            buffer.writeShort(category);
        }
        if (modelYaw != -1) {
            buffer.writeByte(95);
            buffer.writeShort(modelYaw);
        }
        if (notedId != -1) {
            buffer.writeByte(97);
            buffer.writeShort(notedId);
        }
        if (notedTemplate != -1) {
            buffer.writeByte(98);
            buffer.writeShort(notedTemplate);
        }
        if (stackIds != null && stackAmounts != null && stackIds.length != 0
                && stackAmounts.length != 0) {
            for (int index = 0; index < stackIds.length; index++) {
                if (stackIds[index] != 0 || stackAmounts[index] != 0) {
                    buffer.writeByte((100 + index));
                    buffer.writeShort(stackIds[index]);
                    buffer.writeShort(stackAmounts[index]);
                }
            }
        }
        if (resizeX != 128) {
            buffer.writeByte(110);
            buffer.writeShort(resizeX);
        }
        if (resizeY != 128) {
            buffer.writeByte(111);
            buffer.writeShort(resizeY);
        }
        if (resizeZ != 128) {
            buffer.writeByte(112);
            buffer.writeShort(resizeZ);
        }
        if (ambient != 1) {
            buffer.writeByte(113);
            buffer.writeByte(ambient);
        }
        if (contrast != 1) {
            buffer.writeByte(114);
            buffer.writeByte(contrast);
        }
        if (teamId != 1) {
            buffer.writeByte(115);
            buffer.writeByte(teamId);
        }
        if (bindId != -1) {
            buffer.writeByte(139);
            buffer.writeShort(bindId);
        }
        if (bindTemplateId != -1) {
            buffer.writeByte(140);
            buffer.writeShort(bindTemplateId);
        }
        if (placeholderId != -1) {
            buffer.writeByte(148);
            buffer.writeShort(placeholderId);
        }
        if (placeholderTemplate != -1) {
            buffer.writeByte(149);
            buffer.writeShort(placeholderTemplate);
        }
        if (parameters != null && !parameters.isEmpty()) {
            buffer.writeByte(249);
            buffer.writeParameters(parameters);
        }
        buffer.writeByte(0);
        return buffer;
    }

    public RenderAnimation getRenderAnimation() {
        return new RenderAnimation(
            getStandAnimation(),
            getStandTurnAnimation(),
            getWalkAnimation(),
            getRotate180Animation(),
            getRotate90Animation(),
            getRotate270Animation(),
            getRunAnimation()
        );
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

    public void setLowercaseName(String lowercaseName) {
        this.lowercaseName = lowercaseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getInventoryOptions() {
        return inventoryOptions;
    }

    public void setInventoryOptions(String[] inventoryOptions) {
        this.inventoryOptions = inventoryOptions;
    }

    public String[] getGroundOptions() {
        return groundOptions;
    }

    public boolean isGrandExchange() {
        return grandExchange;
    }

    public void setGrandExchange(boolean grandExchange) {
        this.grandExchange = grandExchange;
    }

    public boolean isMembers() {
        return isMembers;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNotedTemplate() {
        return notedTemplate;
    }

    public void setNotedTemplate(int notedTemplate) {
        this.notedTemplate = notedTemplate;
    }

    public int getNotedId() {
        return notedId;
    }

    public void setNotedId(int notedId) {
        this.notedId = notedId;
    }

    public int getBindTemplateId() {
        return bindTemplateId;
    }

    public int getBindId() {
        return bindId;
    }

    public int getPlaceholderTemplate() {
        return placeholderTemplate;
    }

    public void setPlaceholderTemplate(int placeholderTemplate) {
        this.placeholderTemplate = placeholderTemplate;
    }

    public int getPlaceholderId() {
        return placeholderId;
    }

    public void setPlaceholderId(int placeholderId) {
        this.placeholderId = placeholderId;
    }

    public int[] getStackIds() {
        return stackIds;
    }

    public void setStackIds(int[] stackIds) {
        this.stackIds = stackIds;
    }

    public int[] getStackAmounts() {
        return stackAmounts;
    }

    public void setStackAmounts(int[] stackAmounts) {
        this.stackAmounts = stackAmounts;
    }

    public int getMaleOffset() {
        return maleOffset;
    }

    public void setMaleOffset(int maleOffset) {
        this.maleOffset = maleOffset;
    }

    public int getPrimaryMaleHeadModelId() {
        return primaryMaleHeadModelId;
    }

    public void setPrimaryMaleHeadModelId(int primaryMaleHeadModelId) {
        this.primaryMaleHeadModelId = primaryMaleHeadModelId;
    }

    public int getSecondaryMaleHeadModelId() {
        return secondaryMaleHeadModelId;
    }

    public void setSecondaryMaleHeadModelId(int secondaryMaleHeadModelId) {
        this.secondaryMaleHeadModelId = secondaryMaleHeadModelId;
    }

    public int getPrimaryMaleModel() {
        return primaryMaleModel;
    }

    public void setPrimaryMaleModel(int primaryMaleModel) {
        this.primaryMaleModel = primaryMaleModel;
    }

    public int getSecondaryMaleModel() {
        return secondaryMaleModel;
    }

    public void setSecondaryMaleModel(int secondaryMaleModel) {
        this.secondaryMaleModel = secondaryMaleModel;
    }

    public int getTertiaryMaleModel() {
        return tertiaryMaleModel;
    }

    public void setTertiaryMaleModel(int tertiaryMaleModel) {
        this.tertiaryMaleModel = tertiaryMaleModel;
    }

    public int getFemaleOffset() {
        return femaleOffset;
    }

    public void setFemaleOffset(int femaleOffset) {
        this.femaleOffset = femaleOffset;
    }

    public int getPrimaryFemaleHeadModelId() {
        return primaryFemaleHeadModelId;
    }

    public void setPrimaryFemaleHeadModelId(int primaryFemaleHeadModelId) {
        this.primaryFemaleHeadModelId = primaryFemaleHeadModelId;
    }

    public int getSecondaryFemaleHeadModelId() {
        return secondaryFemaleHeadModelId;
    }

    public void setSecondaryFemaleHeadModelId(int secondaryFemaleHeadModelId) {
        this.secondaryFemaleHeadModelId = secondaryFemaleHeadModelId;
    }

    public int getPrimaryFemaleModel() {
        return primaryFemaleModel;
    }

    public void setPrimaryFemaleModel(int primaryFemaleModel) {
        this.primaryFemaleModel = primaryFemaleModel;
    }

    public int getSecondaryFemaleModel() {
        return secondaryFemaleModel;
    }

    public void setSecondaryFemaleModel(int secondaryFemaleModel) {
        this.secondaryFemaleModel = secondaryFemaleModel;
    }

    public int getTertiaryFemaleModel() {
        return tertiaryFemaleModel;
    }

    public void setTertiaryFemaleModel(int tertiaryFemaleModel) {
        this.tertiaryFemaleModel = tertiaryFemaleModel;
    }

    public int getInventoryModelId() {
        return inventoryModelId;
    }

    public void setInventoryModelId(int inventoryModelId) {
        this.inventoryModelId = inventoryModelId;
    }

    public int getShiftClickIndex() {
        return shiftClickIndex;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getModelPitch() {
        return modelPitch;
    }

    public void setModelPitch(int modelPitch) {
        this.modelPitch = modelPitch;
    }

    public int getModelRoll() {
        return modelRoll;
    }

    public void setModelRoll(int modelRoll) {
        this.modelRoll = modelRoll;
    }

    public int getModelYaw() {
        return modelYaw;
    }

    public void setModelYaw(int modelYaw) {
        this.modelYaw = modelYaw;
    }

    public int getResizeX() {
        return resizeX;
    }

    public int getResizeY() {
        return resizeY;
    }

    public int getResizeZ() {
        return resizeZ;
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

    public short[] getOriginalTextureIds() {
        return originalTextureIds;
    }

    public short[] getReplacementTextureIds() {
        return replacementTextureIds;
    }

    public int getAmbient() {
        return ambient;
    }

    public int getContrast() {
        return contrast;
    }

    public Int2ObjectMap<Object> getParameters() {
        return parameters;
    }

    public void setParameters(Int2ObjectMap<Object> parameters) {
        this.parameters = parameters;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public float getWeight() {
        return (float) cacheWeight / 1000.0f;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int[] getBonuses() {
        return bonuses;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }

    public int getBlockAnimation() {
        return blockAnimation;
    }

    public int getStandAnimation() {
        return standAnimation;
    }

    public int getWalkAnimation() {
        return walkAnimation;
    }

    public int getRunAnimation() {
        return runAnimation;
    }

    public int getStandTurnAnimation() {
        return standTurnAnimation;
    }

    public int getRotate90Animation() {
        return rotate90Animation;
    }

    public int getRotate180Animation() {
        return rotate180Animation;
    }

    public int getRotate270Animation() {
        return rotate270Animation;
    }

    public int getAccurateAnimation() {
        return accurateAnimation;
    }

    public int getAggressiveAnimation() {
        return aggressiveAnimation;
    }

    public int getControlledAnimation() {
        return controlledAnimation;
    }

    public int getDefensiveAnimation() {
        return defensiveAnimation;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getInterfaceVarbit() {
        return interfaceVarbit;
    }

    public int getNormalAttackDistance() {
        return normalAttackDistance;
    }

    public int getLongAttackDistance() {
        return longAttackDistance;
    }

    public static ItemDefinitions[] getDefinitions() {
        return definitions;
    }

    public static void setDefinitions(ItemDefinitions[] definitions) {
        ItemDefinitions.definitions = definitions;
    }

    public void setGroundOptions(String[] groundOptions) {
        this.groundOptions = groundOptions;
    }

    public void setMembers(boolean members) {
        isMembers = members;
    }

    public int getIsStackable() {
        return isStackable;
    }

    public void setIsStackable(int isStackable) {
        this.isStackable = isStackable;
    }

    public void setBindTemplateId(int bindTemplateId) {
        this.bindTemplateId = bindTemplateId;
    }

    public void setBindId(int bindId) {
        this.bindId = bindId;
    }

    public void setShiftClickIndex(int shiftClickIndex) {
        this.shiftClickIndex = shiftClickIndex;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setResizeX(int resizeX) {
        this.resizeX = resizeX;
    }

    public void setResizeY(int resizeY) {
        this.resizeY = resizeY;
    }

    public void setResizeZ(int resizeZ) {
        this.resizeZ = resizeZ;
    }

    public void setOriginalTextureIds(short[] originalTextureIds) {
        this.originalTextureIds = originalTextureIds;
    }

    public void setReplacementTextureIds(short[] replacementTextureIds) {
        this.replacementTextureIds = replacementTextureIds;
    }

    public void setAmbient(int ambient) {
        this.ambient = ambient;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setBonuses(int[] bonuses) {
        this.bonuses = bonuses;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public void setTwoHanded(boolean twoHanded) {
        this.twoHanded = twoHanded;
    }

    public void setBlockAnimation(int blockAnimation) {
        this.blockAnimation = blockAnimation;
    }

    public void setStandAnimation(int standAnimation) {
        this.standAnimation = standAnimation;
    }

    public void setWalkAnimation(int walkAnimation) {
        this.walkAnimation = walkAnimation;
    }

    public void setRunAnimation(int runAnimation) {
        this.runAnimation = runAnimation;
    }

    public void setStandTurnAnimation(int standTurnAnimation) {
        this.standTurnAnimation = standTurnAnimation;
    }

    public void setRotate90Animation(int rotate90Animation) {
        this.rotate90Animation = rotate90Animation;
    }

    public void setRotate180Animation(int rotate180Animation) {
        this.rotate180Animation = rotate180Animation;
    }

    public void setRotate270Animation(int rotate270Animation) {
        this.rotate270Animation = rotate270Animation;
    }

    public void setAccurateAnimation(int accurateAnimation) {
        this.accurateAnimation = accurateAnimation;
    }

    public void setAggressiveAnimation(int aggressiveAnimation) {
        this.aggressiveAnimation = aggressiveAnimation;
    }

    public void setControlledAnimation(int controlledAnimation) {
        this.controlledAnimation = controlledAnimation;
    }

    public void setDefensiveAnimation(int defensiveAnimation) {
        this.defensiveAnimation = defensiveAnimation;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setInterfaceVarbit(int interfaceVarbit) {
        this.interfaceVarbit = interfaceVarbit;
    }

    public void setNormalAttackDistance(int normalAttackDistance) {
        this.normalAttackDistance = normalAttackDistance;
    }

    public void setLongAttackDistance(int longAttackDistance) {
        this.longAttackDistance = longAttackDistance;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void setLoaded(boolean loaded) {
        ItemDefinitions.loaded = loaded;
    }

    @Override
    public ItemDefinitions clone() {
        try {
            ItemDefinitions clone = (ItemDefinitions) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class ItemDefinitionsBuilder {
        private String name;
        private String lowercaseName;
        private int id;
        private String[] inventoryOptions;
        private String[] groundOptions;
        private boolean grandExchange;
        private boolean isMembers;
        private int isStackable;
        private int price;
        private int notedTemplate;
        private int notedId;
        private int bindTemplateId;
        private int bindId;
        private int placeholderTemplate;
        private int placeholderId;
        private int[] stackIds;
        private int[] stackAmounts;
        private int maleOffset;
        private int primaryMaleHeadModelId;
        private int secondaryMaleHeadModelId;
        private int primaryMaleModel;
        private int secondaryMaleModel;
        private int tertiaryMaleModel;
        private int femaleOffset;
        private int primaryFemaleHeadModelId;
        private int secondaryFemaleHeadModelId;
        private int primaryFemaleModel;
        private int secondaryFemaleModel;
        private int tertiaryFemaleModel;
        private int inventoryModelId;
        private int shiftClickIndex;
        private int teamId;
        private int zoom;
        private int offsetX;
        private int offsetY;
        private int modelPitch;
        private int modelRoll;
        private int modelYaw;
        private int resizeX;
        private int resizeY;
        private int resizeZ;
        private short[] originalColours;
        private short[] replacementColours;
        private short[] originalTextureIds;
        private short[] replacementTextureIds;
        private int ambient;
        private int contrast;
        private Int2ObjectMap<Object> parameters;
        private String examine;
        private float weight;
        private int slot;
        private int[] bonuses;
        private EquipmentType equipmentType;
        private boolean twoHanded;
        private int blockAnimation;
        private int standAnimation;
        private int walkAnimation;
        private int runAnimation;
        private int standTurnAnimation;
        private int rotate90Animation;
        private int rotate180Animation;
        private int rotate270Animation;
        private int accurateAnimation;
        private int aggressiveAnimation;
        private int controlledAnimation;
        private int defensiveAnimation;
        private int attackSpeed;
        private int interfaceVarbit;
        private int normalAttackDistance;
        private int longAttackDistance;

        ItemDefinitionsBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder lowercaseName(final String lowercaseName) {
            this.lowercaseName = lowercaseName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder id(final int id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder inventoryOptions(
                final String[] inventoryOptions) {
            this.inventoryOptions = inventoryOptions;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder groundOptions(final String[] groundOptions) {
            this.groundOptions = groundOptions;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder grandExchange(final boolean grandExchange) {
            this.grandExchange = grandExchange;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder isMembers(final boolean isMembers) {
            this.isMembers = isMembers;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder isStackable(final int isStackable) {
            this.isStackable = isStackable;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder price(final int price) {
            this.price = price;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder notedTemplate(final int notedTemplate) {
            this.notedTemplate = notedTemplate;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder notedId(final int notedId) {
            this.notedId = notedId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder bindTemplateId(final int bindTemplateId) {
            this.bindTemplateId = bindTemplateId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder bindId(final int bindId) {
            this.bindId = bindId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder placeholderTemplate(
                final int placeholderTemplate) {
            this.placeholderTemplate = placeholderTemplate;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder placeholderId(final int placeholderId) {
            this.placeholderId = placeholderId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder stackIds(final int[] stackIds) {
            this.stackIds = stackIds;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder stackAmounts(final int[] stackAmounts) {
            this.stackAmounts = stackAmounts;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder maleOffset(final int maleOffset) {
            this.maleOffset = maleOffset;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder primaryMaleHeadModelId(
                final int primaryMaleHeadModelId) {
            this.primaryMaleHeadModelId = primaryMaleHeadModelId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder secondaryMaleHeadModelId(
                final int secondaryMaleHeadModelId) {
            this.secondaryMaleHeadModelId = secondaryMaleHeadModelId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder primaryMaleModel(final int primaryMaleModel) {
            this.primaryMaleModel = primaryMaleModel;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder secondaryMaleModel(final int secondaryMaleModel) {
            this.secondaryMaleModel = secondaryMaleModel;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder tertiaryMaleModel(final int tertiaryMaleModel) {
            this.tertiaryMaleModel = tertiaryMaleModel;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder femaleOffset(final int femaleOffset) {
            this.femaleOffset = femaleOffset;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder primaryFemaleHeadModelId(
                final int primaryFemaleHeadModelId) {
            this.primaryFemaleHeadModelId = primaryFemaleHeadModelId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder secondaryFemaleHeadModelId(
                final int secondaryFemaleHeadModelId) {
            this.secondaryFemaleHeadModelId = secondaryFemaleHeadModelId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder primaryFemaleModel(final int primaryFemaleModel) {
            this.primaryFemaleModel = primaryFemaleModel;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder secondaryFemaleModel(
                final int secondaryFemaleModel) {
            this.secondaryFemaleModel = secondaryFemaleModel;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder tertiaryFemaleModel(
                final int tertiaryFemaleModel) {
            this.tertiaryFemaleModel = tertiaryFemaleModel;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder inventoryModelId(final int inventoryModelId) {
            this.inventoryModelId = inventoryModelId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder shiftClickIndex(final int shiftClickIndex) {
            this.shiftClickIndex = shiftClickIndex;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder teamId(final int teamId) {
            this.teamId = teamId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder zoom(final int zoom) {
            this.zoom = zoom;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder offsetX(final int offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder offsetY(final int offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder modelPitch(final int modelPitch) {
            this.modelPitch = modelPitch;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder modelRoll(final int modelRoll) {
            this.modelRoll = modelRoll;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder modelYaw(final int modelYaw) {
            this.modelYaw = modelYaw;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder resizeX(final int resizeX) {
            this.resizeX = resizeX;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder resizeY(final int resizeY) {
            this.resizeY = resizeY;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder resizeZ(final int resizeZ) {
            this.resizeZ = resizeZ;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder originalColours(final short[] originalColours) {
            this.originalColours = originalColours;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder replacementColours(
                final short[] replacementColours) {
            this.replacementColours = replacementColours;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder originalTextureIds(
                final short[] originalTextureIds) {
            this.originalTextureIds = originalTextureIds;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder replacementTextureIds(
                final short[] replacementTextureIds) {
            this.replacementTextureIds = replacementTextureIds;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder ambient(final int ambient) {
            this.ambient = ambient;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder contrast(final int contrast) {
            this.contrast = contrast;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder parameters(
                final Int2ObjectMap<Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder examine(final String examine) {
            this.examine = examine;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder weight(final float weight) {
            this.weight = weight;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder slot(final int slot) {
            this.slot = slot;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder bonuses(final int[] bonuses) {
            this.bonuses = bonuses;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder equipmentType(final EquipmentType equipmentType) {
            this.equipmentType = equipmentType;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder twoHanded(final boolean twoHanded) {
            this.twoHanded = twoHanded;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder blockAnimation(final int blockAnimation) {
            this.blockAnimation = blockAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder standAnimation(final int standAnimation) {
            this.standAnimation = standAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder walkAnimation(final int walkAnimation) {
            this.walkAnimation = walkAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder runAnimation(final int runAnimation) {
            this.runAnimation = runAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder standTurnAnimation(final int standTurnAnimation) {
            this.standTurnAnimation = standTurnAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder rotate90Animation(final int rotate90Animation) {
            this.rotate90Animation = rotate90Animation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder rotate180Animation(final int rotate180Animation) {
            this.rotate180Animation = rotate180Animation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder rotate270Animation(final int rotate270Animation) {
            this.rotate270Animation = rotate270Animation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder accurateAnimation(final int accurateAnimation) {
            this.accurateAnimation = accurateAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder aggressiveAnimation(
                final int aggressiveAnimation) {
            this.aggressiveAnimation = aggressiveAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder controlledAnimation(
                final int controlledAnimation) {
            this.controlledAnimation = controlledAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder defensiveAnimation(final int defensiveAnimation) {
            this.defensiveAnimation = defensiveAnimation;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder attackSpeed(final int attackSpeed) {
            this.attackSpeed = attackSpeed;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder interfaceVarbit(final int interfaceVarbit) {
            this.interfaceVarbit = interfaceVarbit;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder normalAttackDistance(
                final int normalAttackDistance) {
            this.normalAttackDistance = normalAttackDistance;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ItemDefinitions.ItemDefinitionsBuilder longAttackDistance(final int longAttackDistance) {
            this.longAttackDistance = longAttackDistance;
            return this;
        }

        public ItemDefinitions build() {
            return new ItemDefinitions(this.name, this.lowercaseName, this.id, this.inventoryOptions,
                    this.groundOptions, this.grandExchange, this.isMembers, this.isStackable, this.price,
                    this.notedTemplate, this.notedId, this.bindTemplateId, this.bindId,
                    this.placeholderTemplate,
                    this.placeholderId, this.stackIds, this.stackAmounts, this.maleOffset,
                    this.primaryMaleHeadModelId, this.secondaryMaleHeadModelId, this.primaryMaleModel,
                    this.secondaryMaleModel, this.tertiaryMaleModel, this.femaleOffset,
                    this.primaryFemaleHeadModelId, this.secondaryFemaleHeadModelId, this.primaryFemaleModel,
                    this.secondaryFemaleModel,
                    this.tertiaryFemaleModel, this.inventoryModelId, this.shiftClickIndex, this.teamId,
                    this.zoom,
                    this.offsetX, this.offsetY, this.modelPitch, this.modelRoll, this.modelYaw, this.resizeX,
                    this.resizeY, this.resizeZ, this.originalColours, this.replacementColours,
                    this.originalTextureIds, this.replacementTextureIds, this.ambient, this.contrast,
                    this.parameters, this.examine, this.weight, this.slot, this.bonuses, this.equipmentType,
                    this.twoHanded,
                    this.blockAnimation, this.standAnimation, this.walkAnimation, this.runAnimation,
                    this.standTurnAnimation, this.rotate90Animation, this.rotate180Animation,
                    this.rotate270Animation, this.accurateAnimation, this.aggressiveAnimation,
                    this.controlledAnimation,
                    this.defensiveAnimation, this.attackSpeed, this.interfaceVarbit,
                    this.normalAttackDistance,
                    this.longAttackDistance);
        }

        @Override
        public String toString() {
            return "ItemDefinitions.ItemDefinitionsBuilder(name=" + this.name + ", lowercaseName="
                    + this.lowercaseName + ", id=" + this.id + ", inventoryOptions="
                    + Arrays.deepToString(this.inventoryOptions) + ", groundOptions="
                    + Arrays.deepToString(this.groundOptions) + ", grandExchange=" + this.grandExchange
                    + ", isMembers=" + this.isMembers + ", isStackable=" + this.isStackable + ", price="
                    + this.price + ", notedTemplate=" + this.notedTemplate + ", notedId=" + this.notedId
                    + ", bindTemplateId=" + this.bindTemplateId + ", bindId=" + this.bindId
                    + ", placeholderTemplate=" + this.placeholderTemplate + ", placeholderId="
                    + this.placeholderId + ", stackIds=" + Arrays.toString(this.stackIds) + ", stackAmounts="
                    + Arrays.toString(this.stackAmounts) + ", maleOffset=" + this.maleOffset
                    + ", primaryMaleHeadModelId=" + this.primaryMaleHeadModelId
                    + ", secondaryMaleHeadModelId=" + this.secondaryMaleHeadModelId + ", primaryMaleModel="
                    + this.primaryMaleModel + ", secondaryMaleModel=" + this.secondaryMaleModel
                    + ", tertiaryMaleModel=" + this.tertiaryMaleModel + ", femaleOffset=" + this.femaleOffset
                    + ", primaryFemaleHeadModelId=" + this.primaryFemaleHeadModelId
                    + ", secondaryFemaleHeadModelId=" + this.secondaryFemaleHeadModelId
                    + ", primaryFemaleModel=" + this.primaryFemaleModel + ", secondaryFemaleModel="
                    + this.secondaryFemaleModel + ", tertiaryFemaleModel=" + this.tertiaryFemaleModel
                    + ", inventoryModelId=" + this.inventoryModelId + ", shiftClickIndex="
                    + this.shiftClickIndex + ", teamId=" + this.teamId + ", zoom=" + this.zoom + ", offsetX="
                    + this.offsetX + ", offsetY=" + this.offsetY + ", modelPitch=" + this.modelPitch
                    + ", modelRoll=" + this.modelRoll + ", modelYaw=" + this.modelYaw + ", resizeX="
                    + this.resizeX + ", resizeY=" + this.resizeY + ", resizeZ=" + this.resizeZ
                    + ", originalColours=" + Arrays.toString(this.originalColours) + ", replacementColours="
                    + Arrays.toString(this.replacementColours) + ", originalTextureIds="
                    + Arrays.toString(this.originalTextureIds) + ", replacementTextureIds="
                    + Arrays.toString(this.replacementTextureIds) + ", ambient=" + this.ambient
                    + ", contrast=" + this.contrast + ", parameters=" + this.parameters + ", examine="
                    + this.examine + ", weight=" + this.weight + ", slot=" + this.slot + ", bonuses="
                    + Arrays.toString(this.bonuses) + ", equipmentType=" + this.equipmentType + ", twoHanded="
                    + this.twoHanded + ", blockAnimation=" + this.blockAnimation + ", standAnimation="
                    + this.standAnimation + ", walkAnimation=" + this.walkAnimation + ", runAnimation="
                    + this.runAnimation + ", standTurnAnimation=" + this.standTurnAnimation
                    + ", rotate90Animation=" + this.rotate90Animation + ", rotate180Animation="
                    + this.rotate180Animation + ", rotate270Animation=" + this.rotate270Animation
                    + ", accurateAnimation=" + this.accurateAnimation + ", aggressiveAnimation="
                    + this.aggressiveAnimation + ", controlledAnimation=" + this.controlledAnimation
                    + ", defensiveAnimation=" + this.defensiveAnimation + ", attackSpeed=" + this.attackSpeed
                    + ", interfaceVarbit=" + this.interfaceVarbit + ", normalAttackDistance="
                    + this.normalAttackDistance + ", longAttackDistance=" + this.longAttackDistance + ")";
        }
    }

    public ItemDefinitions.ItemDefinitionsBuilder toBuilder() {
        return new ItemDefinitions.ItemDefinitionsBuilder()
                .name(this.name)
                .lowercaseName(this.lowercaseName)
                .id(this.id)
                .inventoryOptions(this.inventoryOptions)
                .groundOptions(this.groundOptions)
                .grandExchange(this.grandExchange)
                .isMembers(this.isMembers)
                .isStackable(this.isStackable)
                .price(this.price)
                .notedTemplate(this.notedTemplate)
                .notedId(this.notedId)
                .bindTemplateId(this.bindTemplateId)
                .bindId(this.bindId)
                .placeholderTemplate(this.placeholderTemplate)
                .placeholderId(this.placeholderId)
                .stackIds(this.stackIds)
                .stackAmounts(this.stackAmounts).maleOffset(this.maleOffset)
                .primaryMaleHeadModelId(this.primaryMaleHeadModelId)
                .secondaryMaleHeadModelId(this.secondaryMaleHeadModelId)
                .primaryMaleModel(this.primaryMaleModel).secondaryMaleModel(this.secondaryMaleModel)
                .tertiaryMaleModel(this.tertiaryMaleModel).femaleOffset(this.femaleOffset)
                .primaryFemaleHeadModelId(this.primaryFemaleHeadModelId)
                .secondaryFemaleHeadModelId(this.secondaryFemaleHeadModelId)
                .primaryFemaleModel(this.primaryFemaleModel).secondaryFemaleModel(this.secondaryFemaleModel)
                .tertiaryFemaleModel(this.tertiaryFemaleModel).inventoryModelId(this.inventoryModelId)
                .shiftClickIndex(this.shiftClickIndex).teamId(this.teamId).zoom(this.zoom)
                .offsetX(this.offsetX).offsetY(this.offsetY).modelPitch(this.modelPitch)
                .modelRoll(this.modelRoll).modelYaw(this.modelYaw).resizeX(this.resizeX)
                .resizeY(this.resizeY).resizeZ(this.resizeZ).originalColours(this.originalColours)
                .replacementColours(this.replacementColours).originalTextureIds(this.originalTextureIds)
                .replacementTextureIds(this.replacementTextureIds).ambient(this.ambient)
                .contrast(this.contrast).parameters(this.parameters).examine(this.examine)
                .weight(this.weight).slot(this.slot).bonuses(this.bonuses).equipmentType(this.equipmentType)
                .twoHanded(this.twoHanded).blockAnimation(this.blockAnimation)
                .standAnimation(this.standAnimation).walkAnimation(this.walkAnimation)
                .runAnimation(this.runAnimation).standTurnAnimation(this.standTurnAnimation)
                .rotate90Animation(this.rotate90Animation).rotate180Animation(this.rotate180Animation)
                .rotate270Animation(this.rotate270Animation).accurateAnimation(this.accurateAnimation)
                .aggressiveAnimation(this.aggressiveAnimation).controlledAnimation(this.controlledAnimation)
                .defensiveAnimation(this.defensiveAnimation).attackSpeed(this.attackSpeed)
                .interfaceVarbit(this.interfaceVarbit).normalAttackDistance(this.normalAttackDistance)
                .longAttackDistance(this.longAttackDistance);
    }

    @Override
    public String toString() {
        return "ItemDefinitions{" +
                "name='" + name + '\'' +
                ", lowercaseName='" + lowercaseName + '\'' +
                ", id=" + id +
                ", inventoryOptions=" + Arrays.deepToString(inventoryOptions) +
                ", groundOptions=" + Arrays.deepToString(groundOptions) +
                ", grandExchange=" + grandExchange +
                ", isMembers=" + isMembers +
                ", isStackable=" + isStackable +
                ", price=" + price +
                ", notedTemplate=" + notedTemplate +
                ", notedId=" + notedId +
                ", bindTemplateId=" + bindTemplateId +
                ", bindId=" + bindId +
                ", placeholderTemplate=" + placeholderTemplate +
                ", placeholderId=" + placeholderId +
                ", stackIds=" + Arrays.toString(stackIds) +
                ", stackAmounts=" + Arrays.toString(stackAmounts) +
                ", maleOffset=" + maleOffset +
                ", primaryMaleHeadModelId=" + primaryMaleHeadModelId +
                ", secondaryMaleHeadModelId=" + secondaryMaleHeadModelId +
                ", primaryMaleModel=" + primaryMaleModel +
                ", secondaryMaleModel=" + secondaryMaleModel +
                ", tertiaryMaleModel=" + tertiaryMaleModel +
                ", femaleOffset=" + femaleOffset +
                ", primaryFemaleHeadModelId=" + primaryFemaleHeadModelId +
                ", secondaryFemaleHeadModelId=" + secondaryFemaleHeadModelId +
                ", primaryFemaleModel=" + primaryFemaleModel +
                ", secondaryFemaleModel=" + secondaryFemaleModel +
                ", tertiaryFemaleModel=" + tertiaryFemaleModel +
                ", inventoryModelId=" + inventoryModelId +
                ", shiftClickIndex=" + shiftClickIndex +
                ", teamId=" + teamId +
                ", zoom=" + zoom +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", modelPitch=" + modelPitch +
                ", modelRoll=" + modelRoll +
                ", modelYaw=" + modelYaw +
                ", resizeX=" + resizeX +
                ", resizeY=" + resizeY +
                ", resizeZ=" + resizeZ +
                ", originalColours=" + Arrays.toString(originalColours) +
                ", replacementColours=" + Arrays.toString(replacementColours) +
                ", originalTextureIds=" + Arrays.toString(originalTextureIds) +
                ", replacementTextureIds=" + Arrays.toString(replacementTextureIds) +
                ", ambient=" + ambient +
                ", contrast=" + contrast +
                ", wearPos1=" + wearPos1 +
                ", wearPos2=" + wearPos2 +
                ", wearPos3=" + wearPos3 +
                ", cacheWeight=" + cacheWeight +
                ", category=" + category +
                ", parameters=" + parameters +
                ", examine='" + examine + '\'' +
                ", weight=" + weight +
                ", slot=" + slot +
                ", bonuses=" + Arrays.toString(bonuses) +
                ", equipmentType=" + equipmentType +
                ", twoHanded=" + twoHanded +
                ", blockAnimation=" + blockAnimation +
                ", standAnimation=" + standAnimation +
                ", walkAnimation=" + walkAnimation +
                ", runAnimation=" + runAnimation +
                ", standTurnAnimation=" + standTurnAnimation +
                ", rotate90Animation=" + rotate90Animation +
                ", rotate180Animation=" + rotate180Animation +
                ", rotate270Animation=" + rotate270Animation +
                ", accurateAnimation=" + accurateAnimation +
                ", aggressiveAnimation=" + aggressiveAnimation +
                ", controlledAnimation=" + controlledAnimation +
                ", defensiveAnimation=" + defensiveAnimation +
                ", attackSpeed=" + attackSpeed +
                ", interfaceVarbit=" + interfaceVarbit +
                ", normalAttackDistance=" + normalAttackDistance +
                ", longAttackDistance=" + longAttackDistance +
                '}';
    }

}
