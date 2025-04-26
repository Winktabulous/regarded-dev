package mgi.types.config.enums;

import java.util.function.Supplier;

/**
 * @author Kris | 20/11/2018 20:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 *
 * <p>An interface used for storing constants of enums, as well as helper methods.</p>
 * <p>Type is interface as opposed to a class to prevent unnecessary cluttering of "public static final"...
 * per every constant, as well as to ensure that the server runs flawlessly in the scenario in which
 * one of the enums provided here changes in the cache, making it throw an exception. Lazy class initialization
 * could otherwise create issues.</p>
 */
public interface Enums {

    Supplier<RuntimeException> runtimeExceptionSupplier = RuntimeException::new;

    static Supplier<RuntimeException> exception() {
        return runtimeExceptionSupplier;
    }

    IntEnum ITEM_RETRIEVAL_SERVICE = EnumDefinitions.getIntEnum(1757);
    IntEnum RAIDS_ONLY_ITEMS = EnumDefinitions.getIntEnum(1666);
    IntEnum ITEM_SETS = EnumDefinitions.getIntEnum(1034);
    IntEnum FARMING_WATERING_CANS = EnumDefinitions.getIntEnum(136);
    StringEnum MINIGAMES_LIST = EnumDefinitions.getStringEnum(848);
    IntEnum BANK_EQUIPMENT_TAB_SLOT_MAP = EnumDefinitions.getIntEnum(2777);
    IntEnum CL_BOSSES = EnumDefinitions.getIntEnum(2103);
    StringEnum SKILL_DIALOGUE_STRING = EnumDefinitions.getStringEnum(1809);

    StringEnum EXPERIENCE_TRACKER_COLOURS = EnumDefinitions.getStringEnum(1168);
    StringEnum EXPERIENCE_TRACKER_DURATION = EnumDefinitions.getStringEnum(1166);
    StringEnum EXPERIENCE_TRACKER_SIZE = EnumDefinitions.getStringEnum(1165);
    StringEnum EXPERIENCE_TRACKER_POSITION = EnumDefinitions.getStringEnum(1164);
    StringEnum EXPERIENCE_TRACKER_GROUP = EnumDefinitions.getStringEnum(1170);
    StringEnum EXPERIENCE_TRACKER_SPEED = EnumDefinitions.getStringEnum(1140);

    IntEnum PUZZLE_BOX_ENUMS = EnumDefinitions.getIntEnum(1864);

    IntEnum STASH_UNIT_BUILD_STAGES_VARS = EnumDefinitions.getIntEnum(1440);
    IntEnum STASH_UNIT_BUILD_STAGES_CONTAINER = EnumDefinitions.getIntEnum(1525);

    // IntEnum ITEMS_ALWAYS_LOST_ON_DEATH = EnumDefinitions.getIntEnum(879);

    IntEnum DIANGO_ITEM_RETRIEVAL = EnumDefinitions.getIntEnum(708);

    IntEnum FAKE_XP_DROPS = EnumDefinitions.getIntEnum(681);

    IntEnum TASK_EXTENSION_ENUM = EnumDefinitions.getIntEnum(273);
    IntEnum TASK_COST_ENUM = EnumDefinitions.getIntEnum(836);
    IntEnum TASK_DISABLE_ENUM = EnumDefinitions.getIntEnum(854);
    StringEnumLC SLAYER_PERK_REWARD_NAMES = EnumDefinitions.getStringEnumLowercase(834);
    IntEnum SLAYER_ITEM_REWARDS_ENUM = EnumDefinitions.getIntEnum(840);
    IntEnum SLAYER_REWARDS_COST = EnumDefinitions.getIntEnum(842);

    // StringEnum COSTUME_STORAGE_UNIT_ENUM = EnumDefinitions.getStringEnum(380);

    StringEnum EMOTES_ENUM = EnumDefinitions.getStringEnum(1000);

    StringEnum SKILL_GUIDES_ENUM = EnumDefinitions.getStringEnum(108);

    IntEnum MUSIC_SLOT_INTERFACE_ENUM = EnumDefinitions.getIntEnum(819);
    StringEnum MUSIC_SLOT_NAME_ENUM = EnumDefinitions.getStringEnum(812);

    StringEnum FAIRY_RING_CODES = EnumDefinitions.getStringEnum(823);
    IntEnum FAIRY_RING_VARBIT_CODES = EnumDefinitions.getIntEnum(824);

    IntEnum REGULAR_SPELLS_ENUM = EnumDefinitions.getIntEnum(1982);
    IntEnum ANCIENT_SPELLS_ENUM = EnumDefinitions.getIntEnum(1983);
    IntEnum LUNAR_SPELLS_ENUM = EnumDefinitions.getIntEnum(1984);
    IntEnum ARCEUUS_SPELLS_ENUM = EnumDefinitions.getIntEnum(1985);
    IntEnum AUTOCASTABLE_SPELLS_ENUM = EnumDefinitions.getIntEnum(1986);

    StringEnum PEST_CONTROL_REWARDS_ENUM = EnumDefinitions.getStringEnum(2285);
    IntEnum PEST_CONTROL_POINTS_ENUM = EnumDefinitions.getIntEnum(2286);
    IntEnum PEST_CONTROL_REWARDS_VOID_ELEMENTS_ENUM = EnumDefinitions.getIntEnum(2287);
    IntEnum PEST_CONTROL_REWARDS_PACKS_STATS_ENUM = EnumDefinitions.getIntEnum(2288);
    StringEnum SKILL_NAMES_ENUM = EnumDefinitions.getStringEnum(680);

    IntEnum TOURNAMENT_ITEMS_ENUM = EnumDefinitions.getIntEnum(10024);
    IntEnum TOURNAMENT_REWARDS = EnumDefinitions.getIntEnum(10053);
    IntEnum TOURNAMENT_REWARDS_NUM = EnumDefinitions.getIntEnum(10054);
    IntEnum TOURNAMENT_REWARDS_COST = EnumDefinitions.getIntEnum(10055);
    IntEnum TOURNAMENT_REWARDS_IRONMAN = EnumDefinitions.getIntEnum(10056);

    IntEnum OPHELD_TO_IFBUTTON = EnumDefinitions.getIntEnum(4303);

    IntEnum NON_SEARCHABLE_SETTINGS_CATEGORIES = EnumDefinitions.getIntEnum(423);
    IntEnum SEARCHABLE_SETTINGS_CATEGORIES = EnumDefinitions.getIntEnum(422);

    IntEnum KEYBINDS = EnumDefinitions.getIntEnum(1161);

    IntEnum GIM_STORAGE_REQS = EnumDefinitions.getIntEnum(4216);

    IntEnum TOB_SUPPLIES_SLOT_TO_ITEM = EnumDefinitions.getIntEnum(1952);
    IntEnum TOB_SUPPLIES_ITEM_TO_COST = EnumDefinitions.getIntEnum(1953);

    IntEnum TOURNAMENT_SUPPLIES_BY_CHILD = EnumDefinitions.getIntEnum(1124);

}
