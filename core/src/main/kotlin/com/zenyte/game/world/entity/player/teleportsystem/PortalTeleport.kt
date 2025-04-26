package com.zenyte.game.world.entity.player.teleportsystem

import com.near_reality.util.capitalize
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.dialogue.PlainChat
import com.zenyte.plugins.item.DiceItem
import com.zenyte.utils.TextUtils
import java.util.*

/**
 * @author Tommeh | 13-11-2018 | 17:43
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)}
 */
enum class PortalTeleport(
    val category: TeleportCategory,
    val smallDescription: String,
    largeDescription: String,
    val unlockType: UnlockType,
    val location: Location
) : Teleport {
    LUMBRIDGE(
        TeleportCategory.CITIES,
        "Lumbridge Castle",
        "Location: Lumbridge Castle",
        UnlockType.DEFAULT,
        Location(3222, 3219, 0)
    ),
    DRAYNOR_VILLAGE(
        TeleportCategory.CITIES,
        "Draynor Village Market",
        "Location: Draynor Village Market",
        UnlockType.DEFAULT,
        Location(3080, 3250, 0)
    ),
    VARROCK(
        TeleportCategory.CITIES,
        "Varrock Square",
        "Location: Varrock Square",
        UnlockType.DEFAULT,
        Location(3213, 3424, 0)
    ),
    RELLEKKA(
        TeleportCategory.CITIES,
        "Rellekka Market",
        "Location: Rellekka Market",
        UnlockType.DEFAULT,
        Location(2643, 3677, 0)
    ),
    ARDOUGNE(
        TeleportCategory.CITIES,
        "Ardougne Market",
        "Location: Ardougne Market",
        UnlockType.DEFAULT,
        Location(2661, 3305, 0)
    ),
    CAMELOT(
        TeleportCategory.CITIES,
        "Camelot Castle",
        "Location: Camelot Castle",
        UnlockType.DEFAULT,
        Location(2757, 3478, 0)
    ),
    KOUREND(
        TeleportCategory.CITIES,
        "Kourend Castle",
        "Location: Kourend Castle",
        UnlockType.DEFAULT,
        Location(1643, 3674, 0)
    ),
    LLETYA(
        TeleportCategory.CITIES,
        "Lletya Center",
        "Location: Lletya Center",
        UnlockType.DEFAULT,
        Location(2332, 3172, 0)
    ),
    YANILLE(
        TeleportCategory.CITIES,
        "Yanille Western Wing",
        "Location: Yanille Western Wing",
        UnlockType.DEFAULT,
        Location(2544, 3092, 0)
    ),
    BURTHORPE(
        TeleportCategory.CITIES,
        "Burthorpe Center",
        "Location: Burthorpe Center",
        UnlockType.DEFAULT,
        Location(2899, 3545, 0)
    ),
    TAVERLEY(
        TeleportCategory.CITIES,
        "Taverley Center",
        "Location: Taverley Center",
        UnlockType.DEFAULT,
        Location(2896, 3455, 0)
    ),
    FALADOR(
        TeleportCategory.CITIES,
        "Falador Western Wing",
        "Location: Falador Western Wing",
        UnlockType.DEFAULT,
        Location(2965, 3378, 0)
    ),
    RIMMINGTON(
        TeleportCategory.CITIES,
        "Rimmington Center",
        "Location: Rimmington Center",
        UnlockType.DEFAULT,
        Location(2957, 3216, 0)
    ),
    BRIMHAVEN(
        TeleportCategory.CITIES,
        "Brimhaven Coast",
        "Location: Brimhaven Coast",
        UnlockType.DEFAULT,
        Location(2762, 3166, 0)
    ),
    SHILO_VILLAGE(
        TeleportCategory.CITIES,
        "Shilo Village Entrance",
        "Location: Shilo Village Entrance",
        UnlockType.DEFAULT,
        Location(2865, 2952, 0)
    ),
    POLLNIVNEACH(
        TeleportCategory.CITIES,
        "Pollnivneach Center",
        "Location: Pollnivneach Center",
        UnlockType.DEFAULT,
        Location(3359, 2968, 0)
    ),
    NARDAH(
        TeleportCategory.CITIES,
        "Nardah Center",
        "Location: Nardah Center",
        UnlockType.DEFAULT,
        Location(3434, 2917, 0)
    ),
    CANIFIS(
        TeleportCategory.CITIES,
        "Canifis Center",
        "Location: Canifis Center",
        UnlockType.DEFAULT,
        Location(3494, 3489, 0)
    ),
    MORT_TON(
        TeleportCategory.CITIES,
        "Mort\'ton Center",
        "Location: Mort\'ton Center",
        UnlockType.DEFAULT,
        Location(3488, 3287, 0)
    ) {
        override fun toString(): String {
            return "Mort\'ton"
        }
    },
    AL_KHARID(
        TeleportCategory.CITIES,
        "Al-Kharid Palace Entrance",
        "Location: Al-Kharid Palace Entrance",
        UnlockType.DEFAULT,
        Location(3293, 3186, 0)
    ),
    MOS_LE_HARMLESS(
        TeleportCategory.CITIES,
        "Mos Le\'Harmless Western Wing",
        "Location: Mos Le\'Harmless Western Wing",
        UnlockType.DEFAULT,
        Location(3683, 2972, 0)
    ),
    CATHERBY(
        TeleportCategory.CITIES,
        "Catherby",
        "Location: Catherby Shore",
        UnlockType.DEFAULT,
        Location(2803, 3434, 0)
    ),
    SOPHANEM(
        TeleportCategory.CITIES,
        "Sophanem",
        "Location: Sophanem",
        UnlockType.DEFAULT,
        Location(3304, 2789, 0)
    ),
    CHICKENS(
        TeleportCategory.TRAINING,
        "North of Lumbridge",
        "Location: North of Lumbridge",
        UnlockType.DEFAULT,
        Location(3238, 3294, 0)
    ),
    GOBLINS(
        TeleportCategory.TRAINING,
        "Lumbridge",
        "Location: Lumbridge",
        UnlockType.DEFAULT,
        Location(3260, 3228, 0)
    ),
    ROCK_CRABS(
        TeleportCategory.TRAINING,
        "Keldagrim Entrance",
        "Location: Keldagrim Entrance",
        UnlockType.DEFAULT,
        Location(2710, 3704, 0)
    ),
    AMMONITE_CRABS(
        TeleportCategory.TRAINING,
        "Fossil Island Northern Coast",
        "Location: Fossil Island Northern Coast",
        UnlockType.DEFAULT,
        Location(3703, 3879, 0)
    ),
    SAND_CRABS(
        TeleportCategory.TRAINING,
        "Great Kourend, Hosidius House",
        "Location: Great Kourend, Hosidius House",
        UnlockType.DEFAULT,
        Location(1779, 3476, 0)
    ),
    SWAMP_CRABS(
        TeleportCategory.TRAINING,
        "Slepe",
        "Location: Slepe",
        UnlockType.DEFAULT,
        Location(3743, 3327, 0)
    ),
    TROLLS(
        TeleportCategory.TRAINING,
        "Trollheim",
        "Location: Trollheim",
        UnlockType.DEFAULT,
        Location(2861, 3591, 0)
    ),
    YAKS(
        TeleportCategory.TRAINING,
        "Neitiznot",
        "Location: Neitiznot",
        UnlockType.DEFAULT,
        Location(2332, 3803, 0)
    ),
    EXPERIMENTS(
        TeleportCategory.TRAINING,
        "Experiments",
        "Location: Experiments Cavern",
        UnlockType.DEFAULT,
        Location(3576, 9927, 0)
    ),
    MONKEY_GUARDS(
        TeleportCategory.TRAINING,
        "Ape Atoll, Marim",
        "Location: Ape Atoll, Marim",
        UnlockType.DEFAULT,
        Location(2786, 2786, 0)
    ),
    OGRES(
        TeleportCategory.TRAINING,
        "Combat Training Camp",
        "Location: Combat Training Camp",
        UnlockType.DEFAULT,
        Location(2518, 3366, 0)
    ),
    ICE_TROLLS(
        TeleportCategory.TRAINING,
        "Fremennik Isles",
        "Location: Fremennik Isles",
        UnlockType.DEFAULT,
        Location(2346, 3832, 0)
    ),
    SLAYER_TOWER(
        TeleportCategory.TRAINING,
        "Slayer Tower",
        "Location: Slayer Tower",
        UnlockType.DEFAULT,
        Location(3428, 3532, 0)
    ),
    BANDIT_CAMP(
        TeleportCategory.TRAINING,
        "Bandit camp",
        "Location: Desert Bandit Camp",
        UnlockType.DEFAULT,
        Location(3174, 3002, 0)
    ),
    ELF_CAMP(
        TeleportCategory.TRAINING,
        "Elf camp",
        "Location: Elf camp",
        UnlockType.DEFAULT,
        Location(2203, 3253, 0)
    ),
    DUEL_ARENA(
        TeleportCategory.MINIGAMES,
        "Duel Arena Lobby",
        "Location: Duel Arena Lobby",
        UnlockType.DEFAULT,
        Location(3367, 3267, 0)
    ),
    FIGHT_CAVES(
        TeleportCategory.MINIGAMES,
        "Fight Caves",
        "Location: Fight Caves",
        UnlockType.DEFAULT,
        Location(2444, 5170, 0)
    ),
    PEST_CONTROL(
        TeleportCategory.MINIGAMES,
        "Void Knights\' Outpost",
        "Location: Void Knights\' Outpost",
        UnlockType.DEFAULT,
        Location(2658, 2672, 0)
    ),
    BARROWS(
        TeleportCategory.MINIGAMES,
        "Barrows",
        "Location: Barrows",
        UnlockType.SCROLL,
        Location(3565, 3306, 0)
    ),
    CLAN_WARS(
        TeleportCategory.MINIGAMES,
        "Clan Wars",
        "Location: Clan Wars",
        UnlockType.DEFAULT,
        Location(3370, 3162, 0)
    ),
    WARRIORS_GUILD(
        TeleportCategory.MINIGAMES,
        "Warriors\' Guild",
        "Location: Warriors\' Guild entrance",
        UnlockType.DEFAULT,
        Location(2880, 3546, 0)
    ),
    CHAMBERS_OF_XERIC(
        TeleportCategory.MINIGAMES,
        "Chambers of Xeric",
        "Location: Mount Quidamortem",
        UnlockType.DEFAULT,
        Location(1255, 3559, 0)
    ),
    GIANT_MOLE(
        TeleportCategory.BOSSES,
        "Falador Park Underground",
        "Location: Falador Park Underground",
        UnlockType.DEFAULT,
        Location(1752, 5235, 0)
    ),
    KALPHITE_QUEEN(
        TeleportCategory.BOSSES,
        "Desert, Kalphite Lair Entrance",
        "Location: Desert, Kalphite Lair Entrance",
        UnlockType.DEFAULT,
        Location(3230, 3109, 0)
    ),
    GODWARS(
        TeleportCategory.BOSSES,
        "Godwars Dungeon",
        "Location: Godwars Dungeon",
        UnlockType.SCROLL,
        Location(2912, 3747, 0)
    ),
    CORPOREAL_BEAST(
        TeleportCategory.BOSSES,
        "Corporeal Beast Dungeon",
        "Location: Corporeal Beast Dungeon",
        UnlockType.DEFAULT,
        Location(2967, 4383, 2)
    ),
    ZULRAH(
        TeleportCategory.BOSSES,
        "Zul-Andra",
        "Locaton: Zul-Andra",
        UnlockType.SCROLL,
        Location(2200, 3055, 0)
    ),
    VORKATH(TeleportCategory.BOSSES, "Ungael", "Location: Ungael", UnlockType.DEFAULT, Location(2277, 4036, 0)),
    KRAKEN(
        TeleportCategory.BOSSES,
        "South-West of Piscatoris Fishing C.",
        "Location: South-West of Piscatoris Fishing Colony",
        UnlockType.SCROLL,
        Location(2282, 3614, 0)
    ),
    THERMONUCLEAR_SMOKE_DEVIL(
        TeleportCategory.BOSSES,
        "South of Castle-Wars",
        "Location: South of Castle-Wars",
        UnlockType.DEFAULT,
        Location(2411, 3055, 0)
    ),
    CERBERUS(
        TeleportCategory.BOSSES,
        "Taverley Dungeon, Keymaster",
        "Location: Taverley Dungeon, Keymaster",
        UnlockType.SCROLL,
        Location(1310, 1249, 0)
    ),
    PHANTOM_MUSPAH(
        TeleportCategory.BOSSES,
        "Ghorrock Dungeon",
        "Location: Ghorrock Dungeon",
        UnlockType.DEFAULT,
        Location(2909, 10317, 0)
    ),
    DAGANNOTH_KINGS(
        TeleportCategory.BOSSES,
        "Waterbirth island",
        "Location: Depths of Waterbirth Island",
        UnlockType.SCROLL,
        Location(1913, 4368, 0)
    ),
    CORSAIR_CAVE(
        TeleportCategory.DUNGEONS,
        "Corsair Cove Dungeon",
        "Location: Corsair Cove Dungeon",
        UnlockType.DEFAULT,
        Location(1933, 9009, 1)
    ),
    ASGARNIAN_ICE_CAVES(
        TeleportCategory.DUNGEONS,
        "Asgarnian Ice Caves",
        "Location: Asgarnian Ice Caves",
        UnlockType.DEFAULT,
        Location(3009, 9549, 0)
    ),
    TAVERLEY_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Taverley Dungeon",
        "Location: Taverley Dungeon",
        UnlockType.DEFAULT,
        Location(2884, 9799, 0)
    ),
    ANCIENT_CAVERN(
        TeleportCategory.DUNGEONS,
        "Ancient Cavern",
        "Location: Ancient Cavern",
        UnlockType.DEFAULT,
        Location(1764, 5365, 1)
    ),
    KARAMJA_UNDERGROUND(
        TeleportCategory.DUNGEONS,
        "Karamja Underground",
        "Location: Karamja Underground",
        UnlockType.DEFAULT,
        Location(2861, 9571, 0)
    ),
    BRIMHAVEN_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Brimhaven Dungeon",
        "Location: Brimhaven Dungeon",
        UnlockType.DEFAULT,
        Location(2708, 9564, 0)
    ),
    SMOKE_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Smoke Dungeon",
        "Location: Smoke Dungeon",
        UnlockType.DEFAULT,
        Location(3207, 9378, 0)
    ),
    APE_ATOLL_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Ape Atoll Dungeon",
        "Location: Ape Atoll Dungeon",
        UnlockType.DEFAULT,
        Location(2766, 9103, 0)
    ),
    CHASM_OF_FIRE(
        TeleportCategory.DUNGEONS,
        "Chasm of Fire",
        "Location: Chasm of Fire",
        UnlockType.DEFAULT,
        Location(1435, 10079, 3)
    ),
    FREMENNIK_SLAYER_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Fremennik Slayer Dungeon",
        "Location: Fremennik Slayer Dungeon",
        UnlockType.DEFAULT,
        Location(2807, 10002, 0)
    ),
    STRONGHOLD_SLAYER_CAVE(
        TeleportCategory.DUNGEONS,
        "Gnome Stronghold",
        "Location: Gnome Stronghold",
        UnlockType.DEFAULT,
        Location(2427, 9824, 0)
    ),
    CRASH_SITE_CAVERN(
        TeleportCategory.DUNGEONS,
        "Crash Site Cavern",
        "Location: Gnome Stronghold",
        UnlockType.DEFAULT,
        Location(2126, 5646, 0)
    ),
    WYVERN_CAVE(
        TeleportCategory.DUNGEONS,
        "Fossil Island Underground",
        "Location: Fossil Island Underground",
        UnlockType.DEFAULT,
        Location(3604, 10230, 0)
    ),
    CRABCLAW_CAVES(
        TeleportCategory.DUNGEONS,
        "Kourend Underground",
        "Location: Kourend Underground",
        UnlockType.DEFAULT,
        Location(1647, 9847, 0)
    ),
    KOUREND_CATACOMBS(
        TeleportCategory.DUNGEONS,
        "Kourend Catacombs",
        "Location: Kourend Catacombs",
        UnlockType.DEFAULT,
        Location(1666, 10048, 0)
    ),
    LITHKREN_VAULT(
        TeleportCategory.DUNGEONS,
        "Lithkren Vault",
        "Location: Lithkren Vault",
        UnlockType.DEFAULT,
        Location(1568, 5063, 0)
    ),
    MOURNER_TUNNELS(
        TeleportCategory.DUNGEONS,
        "Mourner Tunnels",
        "Location: Mourner Tunnels",
        UnlockType.DEFAULT,
        Location(2032, 4636, 0)
    ),
    DORGESH_KAAN_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Dorgesh-Kaan Dungeon",
        "Location: Dorgesh-Kaan Dungeon",
        UnlockType.DEFAULT,
        Location(2715, 5240, 0)
    ),
    BRINE_RAT_CAVERN(
        TeleportCategory.DUNGEONS,
        "Brine Rat Cavern",
        "Location: Brine Rat Cavern",
        UnlockType.DEFAULT,
        Location(2693, 10123, 0)
    ),
    OBSERVATORY_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Observatory Dungeon",
        "Location: Observatory Dungeon",
        UnlockType.DEFAULT,
        Location(2335, 9350, 0)
    ),
    WATERFALL_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Waterfall Dungeon",
        "Location: Waterfall Dungeon",
        UnlockType.DEFAULT,
        Location(2575, 9861, 0)
    ),
    EDGEVILLE_DUNGEON(
        TeleportCategory.DUNGEONS,
        "Edgeville Dungeon",
        "Location: Edgeville Dungeon Center",
        UnlockType.DEFAULT,
        Location(3132, 9912, 0)
    ),
    EVIL_CHICKEN_LAIR(
        TeleportCategory.DUNGEONS,
        "Evil Chicken Lair",
        "Location: Evil Chicken Lair",
        UnlockType.DEFAULT,
        Location(2461, 4356, 0)
    ),
    MAGE_BANK(
        TeleportCategory.WILDERNESS,
        "Mage Arena Bank",
        "Location: Mage Arena Bank",
        UnlockType.DEFAULT,
        Location(2539, 4716, 0)
    ),
    FORINTHRY_DUNGEON(
        TeleportCategory.WILDERNESS,
        "Forinthry Dungeon",
        "Location: Forinthry Dungeon, level 17 wilderness.",
        UnlockType.DEFAULT,
        Location(3068, 3652, 0)
    ),
    WESTERN_DRAGONS(
        TeleportCategory.WILDERNESS,
        "Western Dragons",
        "Location: Western dragons, level 10 wilderness.",
        UnlockType.DEFAULT,
        Location(2979, 3595, 0)
    ),
    EASTERN_DRAGONS(
        TeleportCategory.WILDERNESS,
        "Eastern Dragons",
        "Location: Eastern dragons, level 19 wilderness.",
        UnlockType.DEFAULT,
        Location(3346, 3666, 0)
    ),
    COOKS_GUILD(
        TeleportCategory.SKILLING,
        "Cooks\' Guild Entrance",
        "Location: Cooks\' Guild Entrance",
        UnlockType.DEFAULT,
        Location(3143, 3442, 0)
    ),
    CRAFTING_GUILD(
        TeleportCategory.SKILLING,
        "Crafting Guild Entrance",
        "Location: Crafting Guild Entrance",
        UnlockType.DEFAULT,
        Location(2933, 3290, 0)
    ),
    FISHING_GUILD(
        TeleportCategory.SKILLING,
        "Fishing Guild Entrance",
        "Location: Fishing Guild Entrance",
        UnlockType.DEFAULT,
        Location(2611, 3392, 0)
    ),
    WOODCUTTING_GUILD(
        TeleportCategory.SKILLING,
        "Woodcutting Guild Entrance",
        "Location: Woodcutting Guild Entrance",
        UnlockType.DEFAULT,
        Location(1659, 3505, 0)
    ),
    PISCATORIS_FISHING_COLONY(
        TeleportCategory.SKILLING,
        "Piscatoris Fishing Colony",
        "Location: Piscatoris Fishing Colony",
        UnlockType.DEFAULT,
        Location(2344, 3650, 0)
    ),
    TREE_GNOME_STRONGHOLD(
        TeleportCategory.SKILLING,
        "Tree Gnome Stronghold",
        "Location: Tree Gnome Stronghold",
        UnlockType.DEFAULT,
        Location(2461, 3382, 0)
    ),
    FELDIP_HILLS(
        TeleportCategory.SKILLING,
        "Feldip Hills",
        "Location: Feldip Hills",
        UnlockType.DEFAULT,
        Location(2541, 2926, 0)
    ),
    PURE_ESSENCE_MINE(
        TeleportCategory.SKILLING,
        "Pure Essence Mine",
        "Location: Pure Essence Mine",
        UnlockType.DEFAULT,
        Location(2910, 4833, 0)
    ),
    FARMING_GUILD(
        TeleportCategory.SKILLING,
        "Farming Guild",
        "Location: Farming Guild",
        UnlockType.DEFAULT,
        Location(1249, 3719, 0)
    ),
    HARMONY_ISLAND(
        TeleportCategory.SKILLING,
        "Harmony Island",
        "Location: Harmony Island",
        UnlockType.DEFAULT,
        Location(3800, 2829, 0)
    ),
    PORT_PISCARILIUS(
        TeleportCategory.SKILLING,
        "Port Piscarilius",
        "Port Piscarilius Northern Dock",
        UnlockType.DEFAULT,
        Location(1825, 3777, 0)
    ),
    MINING_GUILD(
        TeleportCategory.SKILLING,
        "Mining Guild",
        "Mining Guild",
        UnlockType.DEFAULT,
        Location(3048, 9763, 0)
    ),
    BRAINDEATH_ISLAND(
        TeleportCategory.MISC,
        "Braindeath Island",
        "Location: Braindeath Island Distillery",
        UnlockType.DEFAULT,
        Location(2149, 5097, 0)
    ),
    LUNAR_ISLE(
        TeleportCategory.MISC,
        "Lunar Isle",
        "Location: Lunar Isle",
        UnlockType.DEFAULT,
        Location(2105, 3914, 0)
    ),
    BARBARIAN_OUTPOST(
        TeleportCategory.MISC,
        "Barbarian Outpost",
        "Location: Barbarian Outpost",
        UnlockType.DEFAULT,
        Location(2548, 3569, 0)
    ),
    WATERBIRTH_ISLAND(
        TeleportCategory.MISC,
        "Waterbirth Island",
        "Location: Waterbirth Island",
        UnlockType.DEFAULT,
        Location(2528, 3740, 0)
    ),
    CRANDOR(
        TeleportCategory.MISC,
        "Crandor Island",
        "Location: Crandor Island",
        UnlockType.DEFAULT,
        Location(2834, 3259, 0)
    ),
    ISAFDAR(
        TeleportCategory.MISC,
        "Isafdar",
        "Location: Isafdar, Ilfeen",
        UnlockType.DEFAULT,
        Location(2223, 3211, 0)
    ),
    GAMBLING(TeleportCategory.MISC, "Gambling", "Location: Castle-Wars", UnlockType.DEFAULT, Location(2441, 3090, 0)) {
        override fun onArrival(player: Player) {
            player.dialogueManager.start(PlainChat(player, DiceItem.GAMBLE_WARNING))
        }
    },
    WATSON(
        TeleportCategory.MISC,
        "Watson\'s house",
        "Location: Watson\'s house, Great Kourend",
        UnlockType.DEFAULT,
        Location(1636, 3577, 0)
    ),
    BLAST_FURNACE(
        TeleportCategory.MINIGAMES,
        "Blast Furnace",
        "Location: Keldagrim",
        UnlockType.DEFAULT,
        Location(2931, 10196, 0)
    ),
    WINTERTODT(
        TeleportCategory.BOSSES,
        "Wintertodt Camp",
        "Location: Northern Tundras of Great Kourend",
        UnlockType.DEFAULT,
        Location(1624, 3929, 0)
    ),
    TYRAS_CAMP(
        TeleportCategory.MISC,
        "Tyras Camp",
        "Location: Tyras Camp",
        UnlockType.DEFAULT,
        Location(2186, 3147, 0)
    );

    val largeDescription = largeDescription + "<br>Unlocked by: " + unlockType.formatted

    override fun toString(): String =
        TextUtils.capitalize(
            name
                .lowercase(Locale.getDefault())
                .replace('_', ' ')
        )

    override fun getType() = TeleportType.NEAR_REALITY_PORTAL_TELEPORT

    override fun getDestination() = location

    override fun getLevel() = 0

    override fun getExperience() = 0.0

    override fun getRandomizationDistance() = 2

    override fun getRunes(): Array<Item>? = null

    override fun getWildernessLevel() = Teleport.WILDERNESS_LEVEL

    override fun isCombatRestricted() = false

    companion object {

        private val teleports = EnumMap<TeleportCategory, List<PortalTeleport>>(TeleportCategory::class.java)
            .apply { putAll(values().groupBy { it.category }) }

        @JvmStatic
        operator fun get(category: TeleportCategory) = teleports[category]!!

        @JvmStatic
        fun main(args: Array<String>) {
            for (cat in TeleportCategory.VALUES) {
                println("\"${cat.name.lowercase().capitalize()}\" {")
                for (tele in get(cat)) {
                    println("    \"${
                        tele.name.lowercase().replace('_', ' ').split(' ').joinToString(" ") { it.capitalize() }
                    }\"(-1, ${tele.location.x}, ${tele.location.y}, ${tele.location.plane}, \"\")")
                }
                println("}")
            }
        }
    }
}
