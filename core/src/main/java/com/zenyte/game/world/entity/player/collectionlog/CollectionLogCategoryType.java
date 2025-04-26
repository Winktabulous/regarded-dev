package com.zenyte.game.world.entity.player.collectionlog;

import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.world.entity.player.Player;

import java.util.function.Function;

/**
 * @author Kris | 24/03/2019 13:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum CollectionLogCategoryType {

    ABYSSAL_SIRE(1526, player -> get(player, "Abyssal Sire")),
    ALCHEMICAL_HYDRA(2074, player -> get(player, "Alchemical Hydra")),
    BARROWS_CHESTS(1502, player -> get(player, "Barrows")),
    BRYOPHYTA(1733, player -> get(player, "Bryophyta")),
    CALLISTO_AND_ARTIO(1510, player -> get(player, "Callisto")),
    CERBERUS(1525, player -> get(player, "Cerberus")),
    CHAOS_ELEMENTAL(1513, player -> get(player, "Chaos Elemental")),
    CHAOS_FANATIC(1519, player -> get(player, "Chaos Fanatic")),
    COMMANDER_ZILYANA(1505, player -> get(player, "Commander Zilyana")),
    CORPOREAL_BEAST(1517, player -> get(player, "Corporeal Beast")),
    CRAZY_ARCHAEOLOGIST(1521, player -> get(player, "Crazy Archaeologist")),
    DAGANNOTH_KINGS(new int[] {1509, 1507, 1508}, player -> get(player, "Dagannoth Supreme"),
            player -> get(player, "Dagannoth Prime"),
            player -> get(player, "Dagannoth Rex")),
    THE_FIGHT_CAVES(1522, player -> get(player, "TzTok-Jad")),
    THE_GAUNTLET(new int[] {2354, 2353}, player -> player.getNumericAttribute("corrupted_gauntlet_completions").intValue(), player -> player.getNumericAttribute("gauntlet_completions").intValue()),
    GANODERMIC_BEAST(-1, player -> get(player, "Ganodermic Beast")),
    GENERAL_GRAARDOR(1504, player -> get(player, "General Graardor")),
    GIANT_MOLE(1514, player -> get(player, "Giant Mole")),
    GROTESQUE_GUARDIANS(1669, player -> get(player, "Grotesque Guardians")),
    HESPORI(2075, player -> get(player, "Hespori")),
    THE_INFERNO(1585, player -> get(player, "TzKal-Zuk")),
    KALPHITE_QUEEN(1516, player -> get(player, "Kalphite Queen")),
    KING_BLACK_DRAGON(1514, player -> get(player, "King Black Dragon")),
    KRAKEN(1523, player -> get(player, "Kraken")),
    KREEARRA(1503, player -> get(player, "Kree'Arra")),
    KRIL_TSUTSAROTH(1506, player -> get(player, "K'ril Tsutsaroth")),
    NEX(3269, player -> get(player, "Nex")),
    THE_NIGHTMARE(2664, player -> get(player, "Phosani's Nightmare"), player -> get(player, "The Nightmare")),
    OBOR(1529, player -> get(player, "Obor")),
    RISE_OF_THE_SIX(-1, player -> get(player, "Rise of the Six")),
    SARACHNIS(2233, player -> get(player, "Sarachnis")),
    SCORPIA(1520, player -> get(player, "Scorpia")),
    SKOTIZO(1527, player -> get(player, "Skotizo")),
    TEMPOROSS(2934, player -> get(player, "Tempoross")),
    THERMONUCLEAR_SMOKE_DEVIL(1524, player -> get(player, "Thermonuclear smoke devil")),
    VANSTROM_KLAUSE(-1, player -> get(player, "Vanstrom Klause")),
    DUKE_SUCELLUS(-1, player -> get(player, "Duke Sucellus")),
    VENENATIS_AND_SPINDEL(1511, player -> get(player, "Venenatis")),
    VETION_AND_CALVARION(1512, player -> get(player, "Vet'ion")),
    VORKATH(1691, player -> get(player, "Vorkath")),
    WINTERTODT(1528, player -> get(player, "Wintertodt")),
    ZALCANO(2352, player -> get(player, "Zalcano")),
    ZULRAH(1518, player -> get(player, "Zulrah")),
    CHAMBERS_OF_XERIC(new int[] {1532, 1735}, player -> player.getNumericAttribute("challengechambersofxeric").intValue(), player -> player.getNumericAttribute("chambersofxeric").intValue()),
    TOMBS_OF_AMASCUT(new int[] {3646, 3645, 3647}, player -> get(player, "tombs of amascut: normal mode"),
            player -> get(player, "tombs of amascut: entry mode"), player -> get(player, "tombs of amascut: expert mode")),
    BEGINNER_TREASURE_TRAILS(-1, player -> player.getNumericAttribute("completed beginner treasure trails").intValue()),
    EASY_TREASURE_TRAILS(-1, player -> player.getNumericAttribute("completed easy treasure trails").intValue()),
    MEDIUM_TREASURE_TRAILS(-1, player -> player.getNumericAttribute("completed medium treasure trails").intValue()),
    HARD_TREASURE_TRAILS(-1, player -> player.getNumericAttribute("completed hard treasure trails").intValue()),
    ELITE_TREASURE_TRAILS(-1, player -> player.getNumericAttribute("completed elite treasure trails").intValue()),
    MASTER_TREASURE_TRAILS(-1, player -> player.getNumericAttribute("completed master treasure trails").intValue()),
    HARD_TREASURE_TRAILS_RARE(-1, player -> player.getNumericAttribute("completed hard treasure trails").intValue()),
    ELITE_TREASURE_TRAILS_RARE(-1, player -> player.getNumericAttribute("completed elite treasure trails").intValue()),
    MASTER_TREASURE_TRAILS_RARE(-1, player -> player.getNumericAttribute("completed master treasure trails").intValue()),
    SHARED_TREASURE_TRAIL_REWARDS(-1, player -> {
        int count = 0;
        for (final ClueLevel tier : ClueLevel.values) {
            count += player.getNumericAttribute("completed " + tier.toString().toLowerCase() + " treasure trails").intValue();
        }
        return count;
    }),
    BARBARIAN_ASSAULT(-1, player -> 0),
    BRIMHAVEN_AGILITY_ARENA(-1, player -> 0),
    CASTLE_WARS(-1),
    FISHING_TRAWLER(-1),
    GIANTS_FOUNDRY(-1),
    GNOME_RESTAURANT(-1),
    GUARDIANS_OF_THE_RIFT(-1, player -> 0),
    HALLOWED_SEPULCHRE(-1, player -> 0),
    LAST_MAN_STANDING(-1, player -> 0),
    MAGIC_TRAINING_ARENA(-1),
    MAHOGANY_HOMES(-1),
    PEST_CONTROL(-1),
    ROGUES_DEN(-1),
    SHADES_OF_MORTTON(-1),
    SOUL_WARS(-1, player -> 0),
    TEMPLE_TREKKING(-1),
    TITHE_FARM(-1),
    TROUBLE_BREWING(-1),
    VOLCANIC_MINE(-1, player -> 0),
    AERIAL_FISHING(-1),
    ALL_PETS(-1),
    CAMDOZAAL(-1),
    CHAMPIONS_CHALLENGE(-1),
    CHAOS_DRUIDS(-1),
    CHOMPY_BIRD_HUNTING(-1),
    CREATURE_CREATION(-1),
    CYCLOPES(-1),
    FOSSIL_ISLAND_NOTES(-1),
    GLOUGHS_EXPERIMENTS(-1, player -> get(player, "Tortured Gorilla"), player -> get(player, "Demonic Gorilla")),
    MONKEY_BACKPACKS(-1),
    MOTHERLODE_MINE(-1),
    MY_NOTES(-1),
    RANDOM_EVENTS(-1),
    REVENANTS(-1, player -> get(player, "Revenant")),
    ROOFTOP_AGILITY(-1),
    SHAYZIEN_ARMOUR(-1),
    SHOOTING_STARS(-1),
    SKILLING_PETS(-1),
    SLAYER(-1),
    TZHAAR(-1),
    MISCELLANEOUS(-1);

    public static final CollectionLogCategoryType[] values = values();
    public final int[] varpIds;
    public final Function<Player, Integer>[] function;

    public static int get(final Player player, final String name) {
        return player.getNotificationSettings().getKillcount(name);
    }

    public static int getMultiple(final Player player, final String... names) {
        int totalCount = 0;
        for (var name : names)
            totalCount += get(player, name);
        return totalCount;
    }

    @SafeVarargs
    CollectionLogCategoryType(int varpId, Function<Player, Integer>... function) {
        this(new int[] {varpId}, function);
    }

    @SafeVarargs
    CollectionLogCategoryType(int[] varpIds, Function<Player, Integer>... function) {
        this.varpIds = varpIds;
        this.function = function;
    }

}