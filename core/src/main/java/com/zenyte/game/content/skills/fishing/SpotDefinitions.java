package com.zenyte.game.content.skills.fishing;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.zenyte.game.content.skills.fishing.FishingTool.*;

/**
 * @author Kris | 04/03/2019 22:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum SpotDefinitions {

    NET(870330, new int[] {1530, 7947, 1544, 1517, 1514, 1518, 1521, 1523, 1524, 1532, 1525, 7155, 1528, 7467, 7462, 1517, 3913, 1530, 7459, 7468, 7469, 7948}, new String[] {"Net", "Small Net"}, SMALL_FISHING_NET, FishDefinitions.SHRIMPS, FishDefinitions.ANCHOVIES),
    BASIC_BAIT(1056000, new int[] {7947, 1544, 1514, 1517, 1518, 1532, 1521, 1523, 1524, 1525, 7155, 1528, 7467, 7462, 1517, 3913, 1530, 7459, 7468, 7469, 7948}, new String[] {"Bait"}, FISHING_ROD, FishingBait.FISHING_BAIT, FishDefinitions.SARDINE, FishDefinitions.HERRING),
    BRAINDEATH(-1, new int[] {635}, new String[] {"Fish"}, SMALL_FISHING_NET, FishDefinitions.SEA_SLUG, FishDefinitions.KARAMTHULU),
    RIVER_LURE(923616, new int[] {1512, 3418, 1529, 1531, 1513, 1507, 1527, 1515, 1506, 1508, 1509, 1516, 1526, 3417, 7468, 8524}, new String[] {"Lure"}, FLY_FISHING_ROD, FishingBait.FEATHER, FishDefinitions.TROUT, FishDefinitions.SALMON),
    RIVER_BAIT(305792, new int[] {1512, 1513, 1506, 1529, 1531, 3418, 1515, 1508, 1507, 1527, 1509, 1516, 1526, 3417, 8524}, new String[] {"Bait"}, FISHING_ROD, FishingBait.FISHING_BAIT, FishDefinitions.PIKE),
    CAGE(116129, new int[] {1533, 1510, 2146, 1519, 1522, 7460, 7465, 7199, 3914, 3657, 1535, 7470, 5820}, new String[] {"Cage"}, LOBSTER_POT, FishDefinitions.LOBSTER),
    BASIC_HARPOON(257770, new int[] {1533, 2146, 1510, 1519, 1522, 7460, 7465, 7199, 3914, 3657, 7470, 5820, 4316}, new String[] {"Harpoon"}, HARPOON, FishDefinitions.TUNA, FishDefinitions.SWORDFISH),
    KARAMBWAN(170874, new int[] {4712, 4713, 4714}, new String[] {"Fish"}, KARAMBWAN_VESSEL, FishingBait.RAW_KARAMBWANJI, FishDefinitions.KARAMBWAN),
    KARAMBWANJI(443697, new int[] {4710}, new String[] {"Net"}, SMALL_FISHING_NET, FishDefinitions.KARAMBWANJI),
    FROG_SPAWN(-1, new int[] {1499, 1500, 1497, 1498}, new String[] {"Net", "Small Net"}, SMALL_FISHING_NET, FishDefinitions.FROG_SPAWN),
    SLIMY_AND_CAVE_EEL(-1, new int[] {1497, 1498, 1499, 1500}, new String[] {"Bait"}, FISHING_ROD, FishingBait.FISHING_BAIT, FishDefinitions.SLIMY_EEL, FishDefinitions.CAVE_EEL),
    SLIMY_EEL(-1, new int[] {2653, 2654, 2655}, new String[] {"Bait"}, FISHING_ROD, FishingBait.FISHING_BAIT, FishDefinitions.SLIMY_EEL),
    LAVA_EEL(-1, new int[] {4928}, new String[] {"Bait"}, OILY_FISHING_ROD, FishingBait.FISHING_BAIT, FishDefinitions.LAVA_EEL),
    INFERNAL_EEL(165000, new int[] {7676}, new String[] {"Bait"}, OILY_FISHING_ROD, FishingBait.FISHING_BAIT, FishDefinitions.INFERNAL_EEL),
    SACRED_EEL(99000, new int[] {6488}, new String[] {"Bait"}, FISHING_ROD, FishingBait.FISHING_BAIT, FishDefinitions.SACRED_EEL),
    MONKFISH(138583, new int[] {4316}, new String[] {"Net"}, SMALL_FISHING_NET, FishDefinitions.MONKFISH),
    MINNOW(-1, new int[] {7730, 7731, 7732, 7733}, new String[] {"Small Net"}, SMALL_FISHING_NET, FishDefinitions.MINNOW),
    BARBARIAN_FISH(1280862, new int[] {1542, 7323}, new String[] {"Use-rod"}, BARBARIAN_ROD, new FishingBait[] {FishingBait.ROE, FishingBait.FISH_OFFCUTS, FishingBait.CAVIAR, FishingBait.FEATHER, FishingBait.FISHING_BAIT}, FishDefinitions.LEAPING_TROUT, FishDefinitions.LEAPING_SALMON, FishDefinitions.LEAPING_STURGEON),
    BIG_NET(1147827, new int[] {1511, 5821, 1534, 1520, 7200, 7466, 3915, 7461, 4477, 4476, 5233, 3419}, new String[] {"Net", "Big Net"}, BIG_FISHING_NET, FishDefinitions.MACKERAL, FishDefinitions.COD, FishDefinitions.BASS),
    SHARK_HARPOON(82243, new int[] {1511, 1534, 7200, 1520, 7466, 3915, 7461, 4477, 4476, 5233, 3419}, new String[] {"Harpoon"}, HARPOON, FishDefinitions.SHARK),
    ANGLERFISH(78649, new int[] {6825}, new String[] {"Bait"}, FISHING_ROD, FishingBait.SANDWORMS, FishDefinitions.ANGLERFISH),
    DARK_CRAB(149434, new int[] {1536}, new String[] {"Cage"}, LOBSTER_POT, FishingBait.DARK_FISHING_BAIT, FishDefinitions.DARK_CRAB);

    public static final SpotDefinitions[] values = values();
    private static final Map<String, SpotDefinitions> map = new Object2ObjectOpenHashMap<>();
    private static final IntOpenHashSet npcs = new IntOpenHashSet();

    static {
        for (final SpotDefinitions spot : values) {
            for (final int id : spot.npcIds) {
                npcs.add(id);
                for (final String action : spot.actions) {
                    map.put(id + "|" + action, spot);
                }
            }
        }
    }

    public static final SpotDefinitions get(final String query) {
        return map.get(query);
    }

    private final int baseClueBottleChance;
    private final int[] npcIds;
    private final String[] actions;
    private final FishingTool tool;
    private final FishingBait[] bait;
    private final FishDefinitions[] fish;
    private final FishDefinitions lowestTierFish;

    SpotDefinitions(final int baseClueBottleChance, final int[] npcIds, final String[] actions, final FishingTool tool, final FishDefinitions... fish) {
        this(baseClueBottleChance, npcIds, actions, tool, (FishingBait[]) null, fish);
    }

    SpotDefinitions(final int baseClueBottleChance, final int[] npcIds, final String[] actions, final FishingTool tool, final FishingBait bait, final FishDefinitions... fish) {
        this(baseClueBottleChance, npcIds, actions, tool, new FishingBait[] {bait}, fish);
    }

    SpotDefinitions(final int baseClueBottleChance, final int[] npcIds, final String[] actions, final FishingTool tool, final FishingBait[] bait, final FishDefinitions... fish) {
        this.baseClueBottleChance = baseClueBottleChance;
        this.npcIds = npcIds;
        this.actions = actions;
        this.tool = tool;
        this.bait = bait;
        this.fish = fish;
        this.lowestTierFish = calculateLowestTierFish();
    }

    @NotNull
    private FishDefinitions calculateLowestTierFish() {
        FishDefinitions lowestTierFish = null;
        for (final FishDefinitions f : fish) {
            if (lowestTierFish == null || lowestTierFish.getLevel() > f.getLevel()) {
                lowestTierFish = f;
            }
        }
        assert lowestTierFish != null;
        return lowestTierFish;
    }

    public static IntOpenHashSet getNpcs() {
        return npcs;
    }

    public int getBaseClueBottleChance() {
        return baseClueBottleChance;
    }

    public int[] getNpcIds() {
        return npcIds;
    }

    public String[] getActions() {
        return actions;
    }

    public FishingTool getTool() {
        return tool;
    }

    public FishingBait[] getBait() {
        return bait;
    }

    public FishDefinitions[] getFish() {
        return fish;
    }

    public FishDefinitions getLowestTierFish() {
        return lowestTierFish;
    }
}
