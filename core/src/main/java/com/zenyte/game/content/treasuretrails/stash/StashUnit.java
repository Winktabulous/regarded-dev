package com.zenyte.game.content.treasuretrails.stash;

import com.zenyte.game.content.treasuretrails.clues.EmoteClue;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

import static com.zenyte.game.content.treasuretrails.stash.StashUnitType.*;

/**
 * @author Kris | 27. nov 2017 : 0:45.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum StashUnit {

    GYPSY_ARIS(BEGINNER, 34736, EmoteClue.EMOTE_110),
    IFFIE_NITTER(BEGINNER, 34737, EmoteClue.EMOTE_112),
    BOBS_AXES(BEGINNER, 34738, EmoteClue.EMOTE_113),

    SILVAREA_MINE(EASY, 28961, EmoteClue.EMOTE_70),
    LUMBER_YARD(EASY, 28981, EmoteClue.EMOTE_98),
    EXAM_CENTRE(EASY, 28980, EmoteClue.EMOTE_34),
    AUBURY_SHOP(EASY, 28986, EmoteClue.EMOTE_59),
    VARROCK_PALACE_ENTRANCE(EASY, 28983, EmoteClue.EMOTE_92),
    VARROCK_PALACE(EASY, 28970, EmoteClue.EMOTE_103),
    GRAND_EXCHANGE(EASY, 28985, EmoteClue.EMOTE_52),
    DUEL_ARENA_MUBARIZ(EASY, 28982, EmoteClue.EMOTE_24),

    AL_KHARID_MINE(EASY, 28965, EmoteClue.EMOTE_54),
    LUMBRIDGE_SWAMP(EASY, 28958, EmoteClue.EMOTE_47),
    WIZARDS_TOWER(EASY, 28959, EmoteClue.EMOTE_35),
    DRAYNOR_VILLAGE(EASY, 28960, EmoteClue.EMOTE_104),
    LUMBRIDGE_WINDMILL(EASY, 28967, EmoteClue.EMOTE_96),
    DRAYNOR_VILLAGE_CROSSROAD(EASY, 28968, EmoteClue.EMOTE_45),
    DRAYNOR_MANOR(EASY, 28966, EmoteClue.EMOTE_90),
    PORT_SARIM(EASY, 28964, EmoteClue.EMOTE_33),
    MUDSKIPPER_POINT(EASY, 28963, EmoteClue.EMOTE_100),
    RIMMINGTON(EASY, 28969, EmoteClue.EMOTE_84),
    RIMMINGTON_CROSSROAD(EASY, 28976, EmoteClue.EMOTE_89),
    FALADOR_PARTY_ROOM(EASY, 28972, EmoteClue.EMOTE_46),
    FALADOR_GEMS(EASY, 28984, EmoteClue.EMOTE_99),
    TAVERLEY(EASY, 28973, EmoteClue.EMOTE_29),
    SINCLAIR_MANSION(EASY, 28979, EmoteClue.EMOTE_68),
    CATHERBY_BEEHIVE(EASY, 28974, EmoteClue.EMOTE_60),
    KEEP_LE_FAYE(EASY, 28978, EmoteClue.EMOTE_79),
    LEGENDS_GUILD(EASY, 28962, EmoteClue.EMOTE_22),
    FISHING_GUILD(EASY, 28977, EmoteClue.EMOTE_57),
    ARDOUGNE_WINDMILL(EASY, 28971, EmoteClue.EMOTE_36),
    ARDOUGNE_ZOO(EASY, 28975, EmoteClue.EMOTE_78),

    CANIFIS_CENTER(MEDIUM, 28987, EmoteClue.EMOTE_50),
    MAUSOLEUM(MEDIUM, 28988, EmoteClue.EMOTE_71),
    BARBARIAN_VILLAGE(MEDIUM, 28989, EmoteClue.EMOTE_94),
    CASTLE_WARS_BANK(MEDIUM, 28991, EmoteClue.EMOTE_105),
    GNOME_AGILITY_COURSE(MEDIUM, 28993, EmoteClue.EMOTE_42),
    OBSERVATORY(MEDIUM, 28995, EmoteClue.EMOTE_97),
    CATHERBY_BANK(MEDIUM, 29001, EmoteClue.EMOTE_85),
    DIGSITE_EASTERN_WINCH(MEDIUM, 28997, EmoteClue.EMOTE_18),
    CATHERBY_FISHING_SHOP(MEDIUM, 29003, EmoteClue.EMOTE_41),
    EDGEVILLE_GENERAL_STORE(MEDIUM, 6657, EmoteClue.EMOTE_30),
    ARCEUUS_LIBRARY(MEDIUM, 29007, EmoteClue.EMOTE_108),
    MOUNT_KARUULM(MEDIUM, 34647, EmoteClue.EMOTE_39),
    TAI_BWO_WANNAI(MEDIUM, 28990, EmoteClue.EMOTE_19),
    BARBARIAN_AGILITY(MEDIUM, 28992, EmoteClue.EMOTE_2),
    YANILLE(MEDIUM, 28994, EmoteClue.EMOTE_61),
    COMBAT_TRAINING_CAMP(MEDIUM, 28996, EmoteClue.EMOTE_31),
    ARCHERY_EMPORIUM(MEDIUM, 28998, EmoteClue.EMOTE_40),
    LUMBRIDGE_SLAYER_CAVE(MEDIUM, 29000, EmoteClue.EMOTE_48),
    SEERS_COURTHOUSE(MEDIUM, 29002, EmoteClue.EMOTE_37),
    TZHAAR_EQUIPMENT_STORE(MEDIUM, 29004, EmoteClue.EMOTE_62),
    SHAYZIEN_COMBAT_RING(MEDIUM, 29006, EmoteClue.EMOTE_20),
    DRAYNOR_JAIL(MEDIUM, 29008, EmoteClue.EMOTE_44),

    CHAOS_TEMPLE_WILDERNESS(HARD, 29009, EmoteClue.EMOTE_86),
    TOP_LIGHTHOUSE(HARD, 29011, EmoteClue.EMOTE_25),
    BANDIT_SHOP_WILDERNESS(HARD, 29013, EmoteClue.EMOTE_106),
    JOKUL_TENT(HARD, 29015, EmoteClue.EMOTE_67),
    SHILO_VILLAGE_TENT(HARD, 29017, EmoteClue.EMOTE_26),
    KHARAZI_JUNGLE(HARD, 29019, EmoteClue.EMOTE_1),
    JIGGIG(HARD, 29021, EmoteClue.EMOTE_11),
    HOSIDIUS_MESS(HARD, 29023, EmoteClue.EMOTE_83),
    FISHING_GUILD_BANK(HARD, 29010, EmoteClue.EMOTE_80),
    SOPHANEM_PYRAMID(HARD, 29012, EmoteClue.EMOTE_49),
    MUSA_POINT(HARD, 29014, EmoteClue.EMOTE_81),
    WHITE_WOLF_MOUNTAIN(HARD, 29016, EmoteClue.EMOTE_73),
    HARD_EXAM_CENTRE(HARD, 29018, EmoteClue.EMOTE_55),
    WILDERNESS_VOLCANO(HARD, 29020, EmoteClue.EMOTE_72),
    AGILITY_PYRAMID(HARD, 29022, EmoteClue.EMOTE_5),

    WEST_ARDOUGNE_CHURCH(ELITE, 29024, EmoteClue.EMOTE_93),
    LAVA_MAZE_DUNGEON(ELITE, 29026, EmoteClue.EMOTE_27),
    WARRIOR_GUILD(ELITE, 29028, EmoteClue.EMOTE_82),
    TOP_TROLLHEIM(ELITE, 29032, EmoteClue.EMOTE_107),
    ANCIENT_CAVERN(ELITE, 29034, EmoteClue.EMOTE_63),
    SHAYZIEN_COMMAND_TENT(ELITE, 29036, EmoteClue.EMOTE_87),
    ARDOUGNE_GEM_STALL(ELITE, 29038, EmoteClue.EMOTE_69),
    NEITZIOT_MINE(ELITE, 29025, EmoteClue.EMOTE_64),
    EDGEVILLE_MONESTARY(ELITE, 29029, EmoteClue.EMOTE_3),
    SLAYER_TOWER(ELITE, 29031, EmoteClue.EMOTE_56),
    HEROES_GUILD(ELITE, /*#####*/29033, EmoteClue.EMOTE_66),
    ELITE_LEGENDS_GUILD(ELITE, 29037, EmoteClue.EMOTE_23),
    FIGHT_ARENA_BAR(ELITE, 29039, EmoteClue.EMOTE_9),

    LAVA_DRAGON_ISLE(MASTER, 29040, EmoteClue.EMOTE_74),
    BARROWS_CHEST(MASTER, 29042, EmoteClue.EMOTE_10),
    SOUL_ALTAR(MASTER, 29046, EmoteClue.EMOTE_91),
    ENTRANA_CHURCH(MASTER, 29048, EmoteClue.EMOTE_32),
    KOUREND_CATACOMBS(MASTER, 29052, EmoteClue.EMOTE_88),
    ZAMORAK_GWD(MASTER, 29054, EmoteClue.EMOTE_28),
    MAGIC_AXE_HUT(MASTER, 29056, EmoteClue.EMOTE_38),
    DEATH_ALTAR(MASTER, 29058, EmoteClue.EMOTE_8),
    ELF_CAMP(MASTER, 29050, EmoteClue.EMOTE_21),
    ENCHANTED_VALLEY(MASTER, 29060, EmoteClue.EMOTE_95),
    ZULANDRA(MASTER, 29041, EmoteClue.EMOTE_65),
    WARRIOR_GUILD_BANK(MASTER, 29047, EmoteClue.EMOTE_77),
    TZHAAR_GEM_STORE(MASTER, 29049, EmoteClue.EMOTE_43),
    MUDKNUCKLE_HOUSE(MASTER, 29051, EmoteClue.EMOTE_53),
    KBD_LAIR(MASTER, 29053, EmoteClue.EMOTE_51),
    VARROCK_GARDEN(MASTER, 29055, EmoteClue.EMOTE_15),
    MISS_SCHISM(MASTER, 29059, EmoteClue.EMOTE_17),

    //Disabled stash units:
    TROLLWEISS_MOUNTAIN(ELITE, 29035, /*EmoteClue.EMOTE_75*/null),
    IBANS_TEMPLE(MASTER, /*#####*/29043, /*EmoteClue.EMOTE_6*/null),
    FISHING_PLATFORM(ELITE, 29030, /*EmoteClue.EMOTE_7*/null),
    SHANTAY_PASS(MEDIUM, 28999, /*EmoteClue.EMOTE_58*/null),
    SHADOW_DUNGEON(ELITE, 29027, /*EmoteClue.EMOTE_4*/null),
    SECOND_FLOOR_CASTLE_DRAKAN(MASTER, 29044, null /* TODO: EMOTE_101 */),
    SEVENTH_ROOM_PYRAMIND_PLUNDER(MASTER, 29045, null /* TODO: EMOTE_102 */),
    TOP_WATCHTOWER(MASTER, 29057, null /* TODO: EMOTE_109 */),//Should implement this one tbf. Others not.

    ;

    private final StashUnitType type;
    private static final StashUnit[] values = values();
    private final int objectId;
    private final EmoteClue clue;
    private static final Int2ObjectMap<StashUnit> map = Int2ObjectMaps.unmodifiable((Int2ObjectMap<StashUnit>) CollectionUtils.populateMap(values, new Int2ObjectOpenHashMap<>(), StashUnit::getObjectId));

    StashUnit(StashUnitType type, int objectId, EmoteClue clue) {
        this.type = type;
        this.objectId = objectId;
        this.clue = clue;
    }

    public static Int2ObjectMap<StashUnit> getMap() {
        return map;
    }

    public final boolean isDisabled() {
        return clue == null;
    }

    public StashUnitType getType() {
        return type;
    }

    public int getObjectId() {
        return objectId;
    }

    public EmoteClue getClue() {
        return clue;
    }

}
