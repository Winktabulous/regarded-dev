package com.zenyte.game.content.skills.farming;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.ObjectDefinitions;

import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Kris | 19. okt 2017 : 19:22.13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FarmingPatch {

    PRIF_FLOWER(PatchType.FLOWER_PATCH, 34919, new Location(3292, 6099), -1),
    PRIF_ALLOTMENT_S(PatchType.ALLOTMENT, 34921, new Location(3291, 6096), new PatchGardener(9138, 3), "Southern allotment", -1),
    PRIF_ALLOTMENT_N(PatchType.ALLOTMENT, 34922, new Location(3291, 6103), new PatchGardener(9138, 4), "Northern allotment", -1),
    PRIF_COMPOST(PatchType.COMPOST_BIN, 34920, new Location(3287, 6100, 0), 70),


    RDI_HERB(PatchType.HERB_PATCH, 50071, new Location(3400, 7998, 0), -1),
    DI_HERB(PatchType.HERB_PATCH, 50070, new Location(3358, 8146, 2), -1),
    DIE_HERB(PatchType.HERB_PATCH, 50069, new Location(3358, 8274, 2), -1),


    RDI_ALLOTMENT(PatchType.ALLOTMENT, 50064, new Location(3399, 8007, 0), -1),


    UDI_HERB(PatchType.HERB_PATCH, 50068, new Location(3417, 7784, 0), -1),
    UDI_TREE(PatchType.TREE_PATCH, 50067, new Location(3417, 7789, 0), -1),

    HOME_COMPOST(PatchType.COMPOST_BIN, 50072, new Location(3106, 3503, 0), 69),
    HOME_FLOWER(PatchType.FLOWER_PATCH, 50066, new Location(3100, 3503, 0), -1),
    HOME_HERB(PatchType.HERB_PATCH, 50065, new Location(3096, 3503, 0), -1),
    HOME_ALLOTMENT_S(PatchType.ALLOTMENT, 50062, new Location(3100, 3501, 0), -1),
    HOME_ALLOTMENT_N(PatchType.ALLOTMENT, 50063, new Location(3100, 3506, 0), -1),


    FALADOR_ALLOTMENT_NW(PatchType.ALLOTMENT, 8550, new Location(3052, 3310, 0), new PatchGardener(2663, 3), "North" +
            "-Western allotment", 8),
    FALADOR_ALLOTMENT_SE(PatchType.ALLOTMENT, 8551, new Location(3057, 3305, 0), new PatchGardener(2663, 4), "South" +
            "-Eastern allotment", 9),
    FALADOR_FLOWER(PatchType.FLOWER_PATCH, 7847, new Location(3054, 3307, 0), 27),
    FALADOR_HERB(PatchType.HERB_PATCH, 8150, new Location(3058, 3311, 0), 43),
    CATHERBY_FLOWER(PatchType.FLOWER_PATCH, 7848, new Location(2809, 3463, 0), 28),

    PHASMATYS_ALLOTMENT_NW(PatchType.ALLOTMENT, 8556, new Location(3599, 3528, 0), new PatchGardener(2666, 3), "North" +
            "-Western allotment", 14),
    PHASMATYS_ALLOTMENT_SE(PatchType.ALLOTMENT, 8557, new Location(3604, 3523, 0), new PatchGardener(2666, 4), "South" +
            "-Eastern allotment", 15),
    PHASMATYS_FLOWER(PatchType.FLOWER_PATCH, 7850, new Location(3601, 3525, 0), 30),
    PHASMATYS_HERB(PatchType.HERB_PATCH, 8153, new Location(3605, 3529, 0), 46),
    CATHERBY_ALLOTMENT_N(PatchType.ALLOTMENT, 8552, new Location(2810, 3466, 0), new PatchGardener(2664, 3),
            "Northern allotment", 10),
    CATHERBY_ALLOTMENT_S(PatchType.ALLOTMENT, 8553, new Location(2810, 3461, 0), new PatchGardener(2664, 4),
            "Southern allotment", 11),
    CATHERBY_HERB(PatchType.HERB_PATCH, 8151, new Location(2813, 3463, 0), 44),
    ARDOUGNE_ALLOTMENT_N(PatchType.ALLOTMENT, 8554, new Location(2667, 3377, 0), new PatchGardener(2665, 3),
            "Northern allotment", 12),
    ARDOUGNE_ALLOTMENT_S(PatchType.ALLOTMENT, 8555, new Location(2667, 3372, 0), new PatchGardener(2665, 4),
            "Southern allotment", 13),
    ARDOUGNE_FLOWER(PatchType.FLOWER_PATCH, 7849, new Location(2666, 3374, 0), 29),
    ARDOUGNE_HERB(PatchType.HERB_PATCH, 8152, new Location(2670, 3374, 0), 45),

    KOUREND_ALLOTMENT_NE(PatchType.ALLOTMENT, 27113, new Location(1736, 3558, 0), new PatchGardener(6921, 3), "North" +
            "-Eastern allotment", 51),
    KOUREND_ALLOTMENT_SW(PatchType.ALLOTMENT, 27114, new Location(1732, 3551, 0), new PatchGardener(6921, 4), "South" +
            "-Western allotment", 52),
    KOUREND_FLOWER(PatchType.FLOWER_PATCH, 27111, new Location(1734, 3554, 0), 53),
    KOUREND_HERB(PatchType.HERB_PATCH, 27115, new Location(1738, 3550, 0), 54),
    KOUREND_COMPOST(PatchType.COMPOST_BIN, 27112, new Location(1730, 3558, 0), 55),


    HARMONY_ALLOTMENT(PatchType.ALLOTMENT, 21950, new Location(3794, 2836, 0), 142),
    HARMONY_HERB(PatchType.HERB_PATCH, 9372, new Location(3789, 2837, 0), 143),
    TROLL_STRONGHOLD_HERB(PatchType.HERB_PATCH, 18816, new Location(2826, 3694, 0), 47),
    LUMBRIDGE_HOPS(PatchType.HOPS_PATCH, 8175, new Location(3229, 3315, 0), new PatchGardener(2672, 3), "hops", 18),
    MCGRUBOR_HOPS(PatchType.HOPS_PATCH, 8176, new Location(2666, 3525, 0), new PatchGardener(2673, 3), "hops", 19),
    YANILLE_HOPS(PatchType.HOPS_PATCH, 8173, new Location(2575, 3104, 0), new PatchGardener(2671, 3), "hops", 16),
    ENTRANA_HOPS(PatchType.HOPS_PATCH, 8174, new Location(2810, 3336, 0), new PatchGardener(2667, 3), "hops", 17),
    CHAMPIONS_GUILD_BUSH(PatchType.BUSH_PATCH, 7577, new Location(3181, 3357, 0), new PatchGardener(2674, 3), "bush",
            20),
    RIMMINGTON_BUSH(PatchType.BUSH_PATCH, 7578, new Location(2940, 3221, 0), new PatchGardener(2675, 3), "bush", 21),
    ARDOUGNE_BUSH(PatchType.BUSH_PATCH, 7580, new Location(2617, 3225, 0), new PatchGardener(2677, 3), "bush", 23),
    ETCETERIA_SPIRIT_TREE(PatchType.SPIRIT_TREE_PATCH, 8382, new Location(2612, 3857, 0), new PatchGardener(2685, 3),
            "spirit tree", 25),
    ETCETERIA_BUSH(PatchType.BUSH_PATCH, 7579, new Location(2591, 3863, 0), new PatchGardener(2676, 3), "bush", 22),
    LUMBRIDGE_TREE(PatchType.TREE_PATCH, 8391, new Location(3193, 3231, 0), new PatchGardener(2681, 3), "tree", 3),
    VARROCK_TREE(PatchType.TREE_PATCH, 8390, new Location(3228, 3458, 0), new PatchGardener(2680, 3), "tree", 2),
    FALADOR_TREE(PatchType.TREE_PATCH, 8389, new Location(3004, 3373, 0), new PatchGardener(2679, 3), "tree", 1),
    TAVERLEY_TREE(PatchType.TREE_PATCH, 8388, new Location(2936, 3438, 0), new PatchGardener(2678, 3), "tree", 0),
    GNOME_STRONGHOLD_TREE(PatchType.TREE_PATCH, 19147, new Location(2436, 3415, 0), new PatchGardener(2687, 3), "tree"
            , 4),
    GNOME_STRONGHOLD_FRUIT_TREE(PatchType.FRUIT_TREE_PATCH, 7962, new Location(2475, 3445, 0), new PatchGardener(2682
            , 3), "fruit tree", 48),
    CATHERBY_FRUIT_TREE(PatchType.FRUIT_TREE_PATCH, 7965, new Location(2860, 3433, 0), new PatchGardener(2670, 3),
            "fruit tree", 7),
    GNOME_MAZE_FRUIT_TREE(PatchType.FRUIT_TREE_PATCH, 7963, new Location(2489, 3179, 0), new PatchGardener(2683, 3),
            "fruit tree", 5),
    BRIMHAVEN_FRUIT_TREE(PatchType.FRUIT_TREE_PATCH, 7964, new Location(2764, 3212, 0), new PatchGardener(2669, 3),
            "fruit tree", 6),
    BRIMHAVEN_SPIRIT_TREE(PatchType.SPIRIT_TREE_PATCH, 8383, new Location(2802, 3203, 0), new PatchGardener(2686, 3),
            "spirit tree", 26),
    LLETYA_FRUIT_TREE(PatchType.FRUIT_TREE_PATCH, 26579, new Location(2346, 3161, 0), new PatchGardener(2689, 3),
            "fruit tree", 49),
    PORT_SARIM_SPIRIT_TREE(PatchType.SPIRIT_TREE_PATCH, 8338, new Location(3060, 3258, 0), new PatchGardener(2684, 3)
            , "spirit tree", 24),
    KOUREND_SPIRIT_TREE(PatchType.SPIRIT_TREE_PATCH, 27116, new Location(1693, 3542, 0), new PatchGardener(6814, 3),
            "spirit tree", 50),
    HARDWOOD_TREE_W(PatchType.HARDWOOD_TREE_PATCH, 30481, new Location(3702, 3837, 0), new PatchGardener(7756, 3),
            "hardwood tree", 149),
    HARDWOOD_TREE_MID(PatchType.HARDWOOD_TREE_PATCH, 30480, new Location(3708, 3833, 0), new PatchGardener(7755, 3),
            "hardwood tree", 150),
    HARDWOOD_TREE_E(PatchType.HARDWOOD_TREE_PATCH, 30482, new Location(3715, 3835, 0), new PatchGardener(7754, 3),
            "hardwood tree", 151),
    GIANT_SEAWEED_N(PatchType.GIANT_SEAWEED_PATCH, 30500, new Location(3733, 10273, 1), 147),
    GIANT_SEAWEED_S(PatchType.GIANT_SEAWEED_PATCH, 30501, new Location(3733, 10267, 1), 148),
    KOUREND_GRAPE_W1(PatchType.GRAPEVINE_PATCH, 11810, new Location(1800, 3563, 0), 56),
    KOUREND_GRAPE_W2(PatchType.GRAPEVINE_PATCH, 11811, new Location(1800, 3560, 0), 57),
    KOUREND_GRAPE_W3(PatchType.GRAPEVINE_PATCH, 11812, new Location(1800, 3557, 0), 58),
    KOUREND_GRAPE_W4(PatchType.GRAPEVINE_PATCH, 11813, new Location(1800, 3554, 0), 59),
    KOUREND_GRAPE_W5(PatchType.GRAPEVINE_PATCH, 11814, new Location(1800, 3551, 0), 60),
    KOUREND_GRAPE_W6(PatchType.GRAPEVINE_PATCH, 11815, new Location(1800, 3548, 0), 61),
    KOUREND_GRAPE_E1(PatchType.GRAPEVINE_PATCH, 11816, new Location(1815, 3563, 0), 62),
    KOUREND_GRAPE_E2(PatchType.GRAPEVINE_PATCH, 11817, new Location(1815, 3560, 0), 63),
    KOUREND_GRAPE_E3(PatchType.GRAPEVINE_PATCH, 11947, new Location(1815, 3557, 0), 64),
    KOUREND_GRAPE_E4(PatchType.GRAPEVINE_PATCH, 12598, new Location(1815, 3554, 0), 65),
    KOUREND_GRAPE_E5(PatchType.GRAPEVINE_PATCH, 12599, new Location(1815, 3551, 0), 66),
    KOUREND_GRAPE_E6(PatchType.GRAPEVINE_PATCH, 12600, new Location(1815, 3548, 0), 67),
    CANIFIS_MUSHROOM(PatchType.MUSHROOM_PATCH, 8337, new Location(3451, 3472, 0), 37),
    ALKHARID_CACTUS(PatchType.CACTUS_PATCH, 7771, new Location(3315, 3202, 0), new PatchGardener(310, 3), "cactus", 36),
    DRAYNOR_MANOR_BELLADONNA(PatchType.BELLADONNA_PATCH, 7572, new Location(3086, 3354, 0), 38),
    TAI_BWO_WANNAI_CALQUAT(PatchType.CALQUAT_PATCH, 7807, new Location(2796, 3101, 0), new PatchGardener(2688, 3),
            "calquat", 31),
    FALADOR_COMPOST(PatchType.COMPOST_BIN, 7836, new Location(3056, 3312, 0), 39),
    PORT_PHASMATYS_COMPOST(PatchType.COMPOST_BIN, 7838, new Location(3610, 3522, 0), 41),
    ARDOUGNE_COMPOST(PatchType.COMPOST_BIN, 7839, new Location(2661, 3375, 0), 42),
    CATHERBY_COMPOST(PatchType.COMPOST_BIN, 7837, new Location(2804, 3464, 0), 40),
    FARMING_GUILD_BIG_COMPOST(PatchType.COMPOST_BIN, 34631, new Location(1272, 3730, 0), 68),
    FARMING_GUILD_REDWOOD(PatchType.REDWOOD_PATCH, new int[]{34051, 34052, 34053, 34054, 34055, 34056, 34057, 34058,
            34059, 34633, 34635, 34639, 34637}, new Location(1228, 3755, 0), new PatchGardener(8536, 3), "redwood " +
            "tree", -1),
    FARMING_GUILD_FLOWER(PatchType.FLOWER_PATCH, 33649, new Location(1260, 3725, 0), -1),
    FARMING_GUILD_BUSH(PatchType.BUSH_PATCH, 34006, new Location(1260, 3733, 0), new PatchGardener(8535, 3), "bush " +
            "patch", -1),
    FARMING_GUILD_SOUTH_ALLOTMENT(PatchType.ALLOTMENT, 33693, new Location(1269, 3726, 0), new PatchGardener(8535, 3)
            , "southern allotment", -1),
    FARMING_GUILD_NORTH_ALLOTMENT(PatchType.ALLOTMENT, 33694, new Location(1269, 3734, 0), new PatchGardener(8535, 3)
            , "northern allotment", -1),
    FARMING_GUILD_CACTUS(PatchType.CACTUS_PATCH, 33761, new Location(1264, 3747, 0), new PatchGardener(8535, 3),
            "cactus patch", -1),
    FARMING_GUILD_HERB(PatchType.HERB_PATCH, 33979, new Location(1238, 3726, 0), -1),
    FARMING_GUILD_TREE(PatchType.TREE_PATCH, 33732, new Location(1232, 3736, 0), new PatchGardener(8534, 3), "tree",
            -1),
    FARMING_GUILD_ANIMA(PatchType.ANIMA_PATCH, 33998, new Location(1232, 3723, 0), -1),
    FARMING_GUILD_SPIRIT_TREE(PatchType.SPIRIT_TREE_PATCH, 33733, new Location(1253, 3750, 0), new PatchGardener(8537
            , 3), "spirit tree", -1),
    FARMING_GUILD_FRUIT_TREE(PatchType.FRUIT_TREE_PATCH, 34007, new Location(1242, 3758, 0), new PatchGardener(8533,
            3), "fruit tree", -1),
    FARMING_GUILD_CELASTRUS(PatchType.CELASTRUS_PATCH, 34629, new Location(1244, 3750, 0), new PatchGardener(8629, 3)
            , "celstrus tree", -1),
    FARMING_GUILD_HESPORI(PatchType.HESPORI_PATCH, 34630, new Location(1247, 10087, 0), -1),
    WEISS_HERB_PATCH(PatchType.HERB_PATCH, 33176, new Location(2848, 3934, 0), -1);
    private static final Int2ObjectOpenHashMap<Set<FarmingPatch>> patchSetByGardeners = new Int2ObjectOpenHashMap<>();
    private final PatchType type;
    private final int[] ids;
    private final int index;
    private final Location center;
    private final Rectangle rectangle;
    private final PatchGardener gardener;
    public static final FarmingPatch[] values = values();
    public static final Int2ObjectOpenHashMap<FarmingPatch> PATCHES = new Int2ObjectOpenHashMap<FarmingPatch>();
    private static final Int2ObjectOpenHashMap<FarmingPatch> gardeners = new Int2ObjectOpenHashMap<FarmingPatch>();
    private final String description;

    public boolean isCompostBin() {
        return type == PatchType.COMPOST_BIN;
    }

    static {
        for (final FarmingPatch p : values) {
            for (int id : p.ids) {
                PATCHES.put(id, p);
            }
            if (p.gardener != null) {
                gardeners.put(p.gardener.getNpcId() | p.gardener.getOption() << 16, p);
                Set<FarmingPatch> set = patchSetByGardeners.get(p.gardener.getNpcId());
                if (set == null) {
                    set = new LinkedHashSet<>(2);
                    patchSetByGardeners.put(p.gardener.getNpcId(), set);
                }
                set.add(p);
            }
        }
    }

    public int getVarbit() {
        assert ids.length > 0;
        return Objects.requireNonNull(ObjectDefinitions.get(ids[0])).getVarbit();
    }

    public static final Optional<FarmingPatch> getPatchByGardener(final int id, final int option) {
        final FarmingPatch patch = gardeners.get(id | option << 16);
        return patch == null ? Optional.empty() : Optional.of(patch);
    }

    public static final Optional<Set<FarmingPatch>> getPatchSetByGardeners(final int id) {
        final Set<FarmingPatch> set = patchSetByGardeners.get(id);
        return set == null ? Optional.empty() : Optional.of(set);
    }

    FarmingPatch(final PatchType type, final int id, final Location center, final int index) {
        this(type, id, center, null, null, index);
    }

    FarmingPatch(final PatchType type, final int id, final Location center, final PatchGardener gardener,
                 final String description, final int index) {
        this(type, new int[]{id}, center, gardener, description, index);
    }

    FarmingPatch(final PatchType type, final int[] ids, final Location center, final PatchGardener gardener,
                 final String description, final int index) {
        this.type = type;
        this.ids = ids;
        this.index = index;
        this.gardener = gardener;
        this.description = description;
        this.center = center;
        rectangle = World.getRectangle(center.getX() - 20, center.getX() + 20, center.getY() - 20, center.getY() + 20);
    }

    public static Int2ObjectOpenHashMap<Set<FarmingPatch>> getPatchSetByGardeners() {
        return patchSetByGardeners;
    }

    public PatchType getType() {
        return type;
    }

    public int[] getIds() {
        return ids;
    }

    public int getIndex() {
        return index;
    }

    public PatchGardener getGardener() {
        return gardener;
    }

    public String getDescription() {
        return description;
    }

    public Location getCenter() {
        return center;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }


    public static final class PatchGardener {
        PatchGardener(final int npcId, final int option) {
            this.npcId = npcId;
            this.option = option;
        }

        private final int npcId;
        private final int option;

        public int getNpcId() {
            return npcId;
        }

        public int getOption() {
            return option;
        }
    }
}
