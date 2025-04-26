package mgi.tools.parser;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

/**
 * @author Tommeh | 16/01/2020 | 19:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum TypeProperty {

    ID("id"),
    NAME("name"),
    OP_1("op1"),
    OP_2("op2"),
    OP_3("op3"),
    OP_4("op4"),
    OP_5("op5"),
    FILTERED_OP_1("filteredop1"),
    FILTERED_OP_2("filteredop2"),
    FILTERED_OP_3("filteredop3"),
    FILTERED_OP_4("filteredop4"),
    FILTERED_OP_5("filteredop5"),
    COMBAT_LEVEL("combatlevel", "combatLevel"),
    FAMILIAR("familiar", "isFamiliar"),
    MINIMAP_VISIBLE("minimapvisible", "minimapVisible"),
    SIZE("size"),
    RESIZE_X("resizex", "resizeX"),
    RESIZE_Y("resizey", "resizeY"),
    RESIZE_Z("resizez", "resizeZ"),
    VARBIT("varbit", "varbit"),
    VARP("varp"),
    DEFAULT_ID("finalTransformation"),
    TRANSFORMED_IDS("transformedids", "transformedIds"),
    TRANSMOGRIFIED_IDS("transmogrifiedids", "transmogrifiedIds"),
    STAND_ANIM("standanimation", "standAnimation"),
    WALK_ANIM("walkanimation", "walkAnimation"),
    ROTATE_90_ANIM("rotate90animation", "rotate90Animation"),
    ROTATE_180_ANIM("rotate180animation", "rotate180Animation"),
    ROTATE_270_ANIM("rotate270animation", "rotate270Animation"),
    MODELS("models"),
    CHAT_MODELS("chatmodels", "chatModels"),
    MODEL_COLOURS("modelcolours", "modelColours"),
    STACK_IDS("stackids", "stackIds"),
    STACK_AMOUNTS("stackamounts", "stackAmounts"),
    ORIGINAL_COLOURS("originalcolours", "originalColours"),
    REPLACEMENT_COLOURS("replacementcolours", "replacementColours"),
    PROJECTILE_CLIP("projectileclip", "projectileClip"),
    OPTIONS_INVISIBLE("optionsinvisible", "optionsInvisible"),
    MODEL_SIZEX("modelsizex", "modelSizeX"),
    MODEL_SIZEY("modelsizey", "modelSizeY"),
    MODEL_HEIGHT("modelheight", "modelSizeHeight"),
    OFFSET_X("offsetx", "offsetX"),
    OFFSET_Y("offsety", "offsetY"),
    OFFSET_HEIGHT("offsetheight", "offsetHeight"),
    MAP_SCENE_ID("mapsceneid", "mapSceneId"),
    SIZE_X("sizex", "sizeX"),
    SIZE_Y("sizey", "sizeY"),
    ACCESS_BLOCK_FLAG("accessblockflag", "accessBlockFlag"),
    PRICE("price"),
    GRAND_EXCHANGE("grandexchange", "grandExchange"),
    STACKABLE("stackable", "isStackable"),
    PARAMETERS("parameters"),
    VALUES("values", "values"),
    PLACEHOLDER_TEMPLATE("placeholdertemplate", "placeholderTemplate"),
    PLACEHOLDER_ID("placeholderid", "placeholderId"),
    INVENTORY_MODEL("invmodel", "inventoryModelId"),
    MEMBERS("members", "isMembers"),
    SHIFT_CLICK_INDEX("shiftclickindex", "shiftClickIndex"),
    ZOOM("zoom"),
    MODEL_ROLL("modelroll", "modelRoll"),
    MODEL_PITCH("modelpitch", "modelPitch"),
    MODEL_YAW("modelyaw", "modelYaw"),
    CLIP_TYPE("cliptype", "clipType"),
    VISIBLE("visible", "clippedMovement"),
    NON_FLAT_SHADING("nonflatshading", "nonFlatShading"),
    CONTOURED_GROUND("contouredground", "contouredGround"),
    AMBIENT("ambient", "ambient"),
    CONTRAST("contrast", "contrast"),
    PRIMARY_MALE_MODEL("primarymalemodel", "primaryMaleModel"),
    PRIMARY_FEMALE_MODEL("primaryfemalemodel", "primaryFemaleModel"),
    SECONDARY_MALE_MODEL("secondarymalemodel", "secondaryMaleModel"),
    SECONDARY_FEMALE_MODEL("secondaryfemalemodel", "secondaryFemaleModel"),
    TERTIARY_MALE_MODEL("tertiarymalemodel", "tertiaryMaleModel"),
    TERTIARY_FEMALE_MODEL("tertiaryfemalemodel", "tertiaryFemaleModel"),
    NOTED_ID("notedid", "notedId"),
    DIRECTION("direction"),
    NOTED_TEMPLATE("notedtemplate", "notedTemplate"),
    OPTIONS_NPC_OBJECT("ops", "options"),
    OPTIONS_ITEM("ops", "inventoryOptions"),
    OPTIONS_ITEM_GROUND("groundops", "groundOptions"),
    FILTERED_OPTIONS("filteredops", "filteredOptions"),
    KEY_TYPE("keytype", "keyType"),
    VALUE_TYPE("valuetype", "valueType"),
    DEFAULT_INT("default", "defaultInt"),
    DEFAULT_STRING("default", "defaultString"),
    PRIORITY("priority"),
    RIGHT_HAND_ITEM("righthanditem", "rightHandItem"),
    LEFT_HAND_ITEM("lefthanditem", "leftHandItem"),
    WIDTH("width"),
    HEIGHT("height"),
    IMAGES("images"),
    X_ALLIGNMENT("xallignment", "xAllignment"),
    Y_ALLIGNMENT("yallignment", "yAllignment"),
    X_MODE("xmode", "xMode"),
    Y_MODE("ymode", "yMode"),
    WIDTH_MODE("widthmode", "widthMode"),
    HEIGHT_MODE("heightmode", "heightMode"),
    FONT("font"),
    TEXT("text"),
    OPBASE("opbase", "opBase"),
    LAYER("layer"),
    X("x"),
    Y("y"),
    FILLED("filled"),
    OPACITY("opacity"),
    ONLOAD("onload"),
    SHADED("shaded", "textShadowed"),
    CLICK_MASK("clickmask", "accessMask"),
    SCROLL_HEIGHT("scrollheight", "scrollHeight"),
    SPRITE("sprite", "spriteId"),
    SPRITETILING("spritetiling", "spriteTiling"),
    TOOLTIP("tooltip"),
    HIDDEN("hidden"),
    BORDER_TYPE("bordertype", "borderType"),
    LINEWIDTH("linewidth", "lineWidth"),
    LINEHEIGHT("lineheight", "lineHeight"),
    ONVARTRANSMIT_TRIGGERS("vartransmittriggers", "varTransmitTriggers"),
    INTERFACE_ID("id", "interfaceId"),
    COMPONENT_ID("componentid", "componentId"),
    BASE_VAR("basevar", "baseVar"),
    START_BIT("startbit", "startBit"),
    END_BIT("endbit", "endBit"),
    HEAD_ICON_ARCHIVE_ID("headIconArchiveIds", "headIconArchiveIds"),
    HEAD_ICON_SPRITE_INDEX("headIconSpriteIndex", "headIconSpriteIndex"),
    ANIMATIONID("animation", "animationId")
    ;

    private final String identifier, field;

    TypeProperty(final String identifier) {
        this(identifier, identifier);
    }


    private static final Object2ObjectOpenHashMap<String, TypeProperty> properties;
    public static final TypeProperty[] values = values();

    static {
        CollectionUtils.populateMap(values, properties = new Object2ObjectOpenHashMap<>(values.length), TypeProperty::getField);
    }

    public static TypeProperty get(final String field) {
        return properties.get(field);
    }

    TypeProperty(String identifier, String field) {
        this.identifier = identifier;
        this.field = field;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getField() {
        return field;
    }
}
