package com.zenyte.game.content.skills.thieving;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ImmutableItem;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Kris | 21. okt 2017 : 12:41.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum PocketData {
    MAN(1, -40, 8, CoinPouch.MAN, true, 5, 1, 1, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.MAN.getItemId())}, "Man", "Woman"),
    FARMER(10, -30, 14.5, CoinPouch.FARMER, true, 5, 1, 1, new String[]{"Cor blimey mate, what are ye doing in me pockets?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.FARMER.getItemId()), new ImmutableItem(5318, 1)}, "Farmer"),
    FEMALE_HAM_MEMBER(15, -25, 18.5, CoinPouch.HAM_MEMBER, true, 4, 1, 3, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(688), new ImmutableItem(686), new ImmutableItem(687), new ImmutableItem(314, 1, 7), new ImmutableItem(882, 1, 15), new ImmutableItem(1351), new ImmutableItem(1205), new ImmutableItem(1265), new ImmutableItem(1129), new ImmutableItem(1349), new ImmutableItem(1203), new ImmutableItem(1267), new ImmutableItem(4298), new ImmutableItem(4300), new ImmutableItem(4302), new ImmutableItem(4304), new ImmutableItem(4306), new ImmutableItem(4308), new ImmutableItem(4310), new ImmutableItem(886, 1, 15), new ImmutableItem(1353), new ImmutableItem(1207), new ImmutableItem(1269), new ImmutableItem(CoinPouch.HAM_MEMBER.getItemId()), new ImmutableItem(946), new ImmutableItem(1511), new ImmutableItem(1733), new ImmutableItem(321, 1, 3), new ImmutableItem(2138), new ImmutableItem(1734, 2, 10), new ImmutableItem(590), new ImmutableItem(1625), new ImmutableItem(453), new ImmutableItem(1739), new ImmutableItem(199), new ImmutableItem(201), new ImmutableItem(203), new ImmutableItem(440), new ImmutableItem(1627)}, 2541) {
        @Override
        public List<Item> generateRandomLoot(final boolean doubleLoot) {
            if (Utils.random(49) == 0) {
                return List.of(new Item(ClueItem.EASY.getScrollBox()));
            }
            return super.generateRandomLoot(doubleLoot);
        }
    },
    MALE_HAM_MEMBER(20, -20, 22.5, CoinPouch.HAM_MEMBER, true, 4, 1, 3, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(688), new ImmutableItem(686), new ImmutableItem(687), new ImmutableItem(314, 1, 7), new ImmutableItem(882, 1, 15), new ImmutableItem(1351), new ImmutableItem(1205), new ImmutableItem(1265), new ImmutableItem(1129), new ImmutableItem(1349), new ImmutableItem(1203), new ImmutableItem(1267), new ImmutableItem(4298), new ImmutableItem(4300), new ImmutableItem(4302), new ImmutableItem(4304), new ImmutableItem(4306), new ImmutableItem(4308), new ImmutableItem(4310), new ImmutableItem(886, 1, 15), new ImmutableItem(1353), new ImmutableItem(1207), new ImmutableItem(1269), new ImmutableItem(CoinPouch.HAM_MEMBER.getItemId()), new ImmutableItem(946), new ImmutableItem(1511), new ImmutableItem(1733), new ImmutableItem(321, 1, 3), new ImmutableItem(2138), new ImmutableItem(1734, 2, 10), new ImmutableItem(590), new ImmutableItem(1625), new ImmutableItem(453), new ImmutableItem(1739), new ImmutableItem(199), new ImmutableItem(201), new ImmutableItem(203), new ImmutableItem(440), new ImmutableItem(1627)}, 2540) {
        @Override
        public List<Item> generateRandomLoot(final boolean doubleLoot) {
            if (Utils.random(49) == 0) {
                return List.of(new Item(ClueItem.EASY.getScrollBox()));
            }
            return super.generateRandomLoot(doubleLoot);
        }
    },
    AL_KHARID_WARRIOR(25, -15, 26, CoinPouch.WARRIOR, true, 5, 2, 2, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.WARRIOR.getItemId())}, NpcId.AL_KHARID_WARRIOR, NpcId.WARRIOR_WOMAN),
    ROGUE(32, -8, 35.5, CoinPouch.ROGUE, true, 5, 2, 2, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.ROGUE.getItemId()), new ImmutableItem(1523), new ImmutableItem(1219), new ImmutableItem(1993), new ImmutableItem(556, 8, 8)}, "Rogue"),
    CAVE_GOBLIN(36, -4, 40, CoinPouch.CAVE_GOBLIN, true, 5, 1, 1, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(10964), new ImmutableItem(10963), new ImmutableItem(10965), new ImmutableItem(10962), new ImmutableItem(10961), new ImmutableItem(10960), new ImmutableItem(CoinPouch.CAVE_GOBLIN.getItemId()), new ImmutableItem(4548), new ImmutableItem(10981), new ImmutableItem(440, 1, 4), new ImmutableItem(4537), new ImmutableItem(1939), new ImmutableItem(590), new ImmutableItem(595)}, "Cave goblin"),
    MASTER_FARMER(38, 38, 43, false, 5, 3, 3, new String[]{"Cor blimey mate, what are ye doing in me pockets?"}, new ImmutableItem[]{
            //The rate of the potato has been lowered(from what it is in Wikia) to allow putting in two more seeds to the table which weren't included prior.
            /*new ImmutableItem(5318, 1, 4, 5.81), new ImmutableItem(5319, 1, 3, 7.47),
                            new ImmutableItem(5324, 1, 3, 14.3), new ImmutableItem(5322, 1, 2, 15.6),
                            new ImmutableItem(5320, 1, 2, 44.8), new ImmutableItem(5323, 1, 1, 82.5),
                            new ImmutableItem(5321, 1, 1, 187), new ImmutableItem(5305, 1, 4, 18),
                            new ImmutableItem(22879, 1, 1, 200),
                            new ImmutableItem(5307, 1, 3, 18), new ImmutableItem(5308, 1, 2, 23.9),
                            new ImmutableItem(5306, 1, 3, 24.1), new ImmutableItem(5309, 1, 1, 36.1),
                            new ImmutableItem(5310, 1, 1, 76.2), new ImmutableItem(5311, 1, 1, 142),
                            new ImmutableItem(5096, 1, 1, 21.8), new ImmutableItem(5098, 1, 1, 32.9),
                            new ImmutableItem(5097, 1, 1, 50.9), new ImmutableItem(5099, 1, 1, 68.9),
                            new ImmutableItem(5100, 1, 1, 86.3), new ImmutableItem(5101, 1, 1, 25.8),
                            new ImmutableItem(5102, 1, 1, 36.8), new ImmutableItem(5103, 1, 1, 51.5),
                            new ImmutableItem(5104, 1, 1, 129), new ImmutableItem(5105, 1, 1, 355),
                            new ImmutableItem(5106, 1, 1, 937),
                            new ImmutableItem(5291, 1, 1, 65.1), new ImmutableItem(5292, 1, 1, 95.6),
                            new ImmutableItem(5293, 1, 1, 140), new ImmutableItem(5294, 1, 1, 206),
                            new ImmutableItem(5295, 1, 1, 302), new ImmutableItem(5296, 1, 1, 443),
                            new ImmutableItem(5297, 1, 1, 651), new ImmutableItem(5298, 1, 1, 947),
                            new ImmutableItem(5299, 1, 1, 1389), new ImmutableItem(5300, 1, 1, 2083),
                            new ImmutableItem(5301, 1, 1, 2976), new ImmutableItem(5302, 1, 1, 4167),
                            new ImmutableItem(5303, 1, 1, 6944), new ImmutableItem(5304, 1, 1, 10417),
                            new ImmutableItem(5282, 1, 1, 400),
                            new ImmutableItem(5281, 1, 1, 667), new ImmutableItem(5280, 1, 1, 1000),
                            new ImmutableItem(22873, 1, 1, 1000)*/
            new ImmutableItem(5280, 1, 1, 1000), //Cactus Seed
            new ImmutableItem(5318, 1, 4, 650),  //Potato Seed
            new ImmutableItem(5291, 1, 1, 600),  //Guam Seed
            new ImmutableItem(5292, 1, 1, 550),  //Marrentill Seed
            new ImmutableItem(5319, 1, 3, 550),  //Onion Seed
            new ImmutableItem(5324, 1, 3, 500),  //Cabbage Seed
            new ImmutableItem(5293, 1, 1, 450),  //Tarromin Seed
            new ImmutableItem(5322, 1, 2, 450),  //Tomato Seed
            new ImmutableItem(5100, 1, 1, 400),  //Limpwurt Seed
            new ImmutableItem(5307, 1, 10, 400),  //Hammerstone Seed
            new ImmutableItem(5308, 1, 10, 400),  //Asgarnian Seed
            new ImmutableItem(5294, 1, 1, 400),  //Harralander Seed
            new ImmutableItem(5320, 1, 2, 350),  //Sweetcorn Seed
            new ImmutableItem(5305, 1, 12, 350),  //Barley Seed
            new ImmutableItem(5306, 1, 10, 340),  //Jute Seed
            new ImmutableItem(5096, 1, 1, 300),  //Marigold Seed
            new ImmutableItem(5101, 1, 1, 300),  //Redberry Seed
            new ImmutableItem(5296, 1, 1, 300),  //Toadflax Seed
            new ImmutableItem(5312, 1, 1, 300),  //Acorn
            new ImmutableItem(5103, 1, 1, 300),  //Dwellberry Seed
            new ImmutableItem(22879, 1, 1, 300),  //Snape grass Seed
            new ImmutableItem(5323, 1, 1, 300),  //Strawberry Seed
            new ImmutableItem(5099, 1, 1, 250),  //Woad Seed
            new ImmutableItem(5282, 1, 1, 250),  //Mushroom  Seed
            new ImmutableItem(5281, 1, 1, 250),  //Belladonna Seed
            new ImmutableItem(5313, 1, 1, 250),  //Willow Seed
            new ImmutableItem(5098, 1, 1, 250),  //Nasturtium Seed
            new ImmutableItem(5309, 1, 10, 250),  //Yanillian Seed
            new ImmutableItem(5283, 1, 1, 250),  //Apple tree Seed
            new ImmutableItem(5106, 1, 1, 250),  //Poison ivy Seed
            new ImmutableItem(5310, 1, 10, 250),  //Krandorian Seed
            new ImmutableItem(5321, 1, 1, 250),  //Watermelon Seed
            new ImmutableItem(5297, 1, 1, 250),  //Irit Seed
            new ImmutableItem(5097, 1, 1, 230),  //Rosemary Seed
            new ImmutableItem(5302, 1, 1, 230),  //Lantadyme Seed
            new ImmutableItem(5298, 1, 1, 230),  //Avantoe Seed
            new ImmutableItem(5102, 1, 1, 225),  //Cadavaberry Seed
            new ImmutableItem(22873, 1, 1, 225),  //Potato cactus Seed
            new ImmutableItem(5303, 1, 1, 210),  //Dwarf weed Seed
            new ImmutableItem(5299, 1, 1, 210),  //Kwuarm Seed
            new ImmutableItem(5301, 1, 1, 200),  //Cadantine Seed
            new ImmutableItem(5284, 1, 1, 200),  //Banana tree Seed
            new ImmutableItem(5311, 1, 10, 200),  //Wildblood Seed
            new ImmutableItem(5295, 1, 1, 200),  //Ranarr Seed
            new ImmutableItem(5104, 1, 1, 190),  //Jangerberry Seed
            new ImmutableItem(5105, 1, 1, 175),  //Whiteberry Seed
            new ImmutableItem(5300, 1, 1, 175),  //Snapdragon Seed
            new ImmutableItem(5285, 1, 1, 175),  //Orange tree Seed
            new ImmutableItem(5290, 1, 1, 150),  //Calquat  Seed
            new ImmutableItem(5287, 1, 1, 150),  //Pineapple Seed
            new ImmutableItem(5286, 1, 1, 150),  //Curry tree Seed
            new ImmutableItem(5314, 1, 1, 125),  //Maple Seed
            new ImmutableItem(5304, 1, 1, 110),  //Torstol Seed
            new ImmutableItem(5288, 1, 1, 95),  //Papaya Seed
            new ImmutableItem(5315, 1, 1, 70),  //Yew Seed
            new ImmutableItem(5289, 1, 1, 55),  //Palm  Seed
            new ImmutableItem(5316, 1, 1, 30)},  //Magic Seed
            "Master Farmer", "Martin the Master Gardener"),
    GUARD(40, 40, 46.8, CoinPouch.GUARD, true, 5, 2, 2, new String[]{
            "What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.GUARD.getItemId())}, "Guard"),
    FREMENNIK_CITIZEN(45, 40, 65, CoinPouch.FREMENNIK, true, 5, 2, 2, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.FREMENNIK.getItemId())}, 3937, 3938, 3939, 3940, 3941, 3942, 3943, 3944, 3945, 3946),
    BEARDED_POLLNIVNIAN_BANDIT(45, 45, 65, CoinPouch.KHARIDIAN_BANDIT, true, 5, 5, 5, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.KHARIDIAN_BANDIT.getItemId())}, 736, 737),
    BANDIT(53, 53, 79.5, CoinPouch.KHARIDIAN_BANDIT, true, 5, 3, 3, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.KHARIDIAN_BANDIT.getItemId()), new ImmutableItem(175, 1), new ImmutableItem(1523, 1)}, "Bandit"),
    KNIGHT(55, 48, 84.3, CoinPouch.KNIGHT_OF_ARDOUGNE, true, 5, 3, 3, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.KNIGHT_OF_ARDOUGNE.getItemId())}, "Knight of Ardougne"),
    POLLNIVNIAN_BANDIT(55, 55, 84.3, CoinPouch.KHARIDIAN_BANDIT, true, 5, 5, 5, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.KHARIDIAN_BANDIT.getItemId())}, 734, 735),
    YANILLE_WATCHMAN(65, 65, 137.5, CoinPouch.WATCHMAN, true, 5, 3, 3, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.WATCHMAN.getItemId()), new ImmutableItem(2309, 1)}, NpcId.WATCHMAN),
    MENAPHITE_THUG(65, 65, 137.5, CoinPouch.KHARIDIAN_BANDIT, true, 5, 5, 5, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.KHARIDIAN_BANDIT.getItemId())}, "Menaphite Thug"),
    PALADIN(70, 70, 151.75, CoinPouch.PALADIN, true, 5, 3, 3, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.PALADIN.getItemId()), new ImmutableItem(562, 2)}, "Paladin"),
    GNOME(75, 75, 198.5, CoinPouch.GNOME, true, 5, 1, 1, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.GNOME.getItemId()), new ImmutableItem(557), new ImmutableItem(444), new ImmutableItem(569), new ImmutableItem(2150), new ImmutableItem(2162)}, "Gnome", "Gnome woman"),
    HERO(80, 80, 275, CoinPouch.HERO, true, 6, 4, 4, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(CoinPouch.HERO.getItemId()), new ImmutableItem(565), new ImmutableItem(1601), new ImmutableItem(1993), new ImmutableItem(560, 2), new ImmutableItem(569), new ImmutableItem(444)}, NpcId.HERO),
    VYRE(82, 82, 306.9, CoinPouch.ELF, true, 6, 4, 4,
            new String[]{"What do you think you're doing?"},
            new ImmutableItem[]{
                    new ImmutableItem(CoinPouch.ELF.getItemId()),
                    new ImmutableItem(995, 250, 315),
                    new ImmutableItem(560, 2, 2, 16),
                    new ImmutableItem(24774, 1, 1, 22),
                    new ImmutableItem(1619, 1, 1, 26.4),
                    new ImmutableItem(565, 4, 4, 66),
                    new ImmutableItem(1601, 1, 1, 132),
                    new ImmutableItem(24785, 1, 1, 132),
            },
            NpcId.CANINELLE_DRAYNAR, NpcId.CARNIVUS_BELAMORTA, NpcId.DIPHYLLA_BECHSTEIN, NpcId.DRACONIS_SANGUINE,
            NpcId.LASENNA_RASPUTIN, NpcId.MORT_NIGHTSHADE, NpcId.MORTINA_DAUBENTON, NpcId.PIPISTRELLE_DRAYNAR, NpcId.REMUS_KANINUS,
            NpcId.VALENTIN_RASPUTIN, NpcId.VALLESSIA_DRACYULA, NpcId.VALLESSIA_VON_PITT, NpcId.VAMPYRESSA_VAN_VON, NpcId.VAMPYRUS_DIAEMUS,
            NpcId.VIOLETTA_SANGUINE, NpcId.VLAD_BECHSTEIN, NpcId.VLAD_DIAEMUS, NpcId.VONNETTA_VARNIS, NpcId.CRIMSONETTE_VAN_MARR, NpcId.GRIGOR_RASPUTIN,
            NpcId.VORMAR_VAKAN, NpcId.ALEK_CONSTANTINE, NpcId.EPISCULA_HELSING, NpcId.HAEMAS_LAMESCUS, NpcId.MISDRIEVUS_SHADUM, NpcId.NAKASA_JOVKAI,
            NpcId.NATALIDAE_SHADUM, NpcId.NOCTILLION_LUGOSI, NpcId.VALENTINA_DIAEMUS, NpcId.VON_VAN_VON) {
        @Override
        public List<Item> generateRandomLoot(final boolean doubleLoot) {
            if (Utils.randomBoolean(3_000)) {
                return List.of(new Item(ItemId.BLOOD_SHARD, doubleLoot ? 2 : 1));
            }
            if (Utils.randomBoolean(1_000)) {
                return List.of(new Item(ItemId.BLOOD_TALISMAN, doubleLoot ? 2 : 1));
            }
            return super.generateRandomLoot(doubleLoot);
        }
    },
    ELF(85, 85, 353, CoinPouch.ELF, false, 6, 5, 5, new String[]{"What do you think you're doing?"},
            new ImmutableItem[] {
                    new ImmutableItem(CoinPouch.ELF.getItemId(), 1, 1, 105),
                    new ImmutableItem(ItemId.DEATH_RUNE, 2, 2, 8),
                    new ImmutableItem(ItemId.JUG_OF_WINE, 1, 1, 6),
                    new ImmutableItem(ItemId.NATURE_RUNE, 3, 3, 5),
                    new ImmutableItem(ItemId.FIRE_ORB, 1, 2),
                    new ImmutableItem(ItemId.DIAMOND, 1, 1, 1),
                    new ImmutableItem(ItemId.GOLD_ORE, 1, 1, 1),
            }, "Arvel", "Mawrth", "Goreu", "Kelyn"),
    ELF_PRIFDDINAS(85, 85, 353, CoinPouch.ELF, false, 6, 5, 5, new String[]{"What do you think you're doing?"},
            new ImmutableItem[] {
                    new ImmutableItem(CoinPouch.ELF.getItemId(), 1, 1, 105),
                    new ImmutableItem(ItemId.DEATH_RUNE, 2, 2, 8),
                    new ImmutableItem(ItemId.JUG_OF_WINE, 1, 1, 6),
                    new ImmutableItem(ItemId.NATURE_RUNE, 3, 3, 5),
                    new ImmutableItem(ItemId.FIRE_ORB, 1, 2),
                    new ImmutableItem(ItemId.DIAMOND, 1, 1, 1),
                    new ImmutableItem(ItemId.GOLD_ORE, 1, 1, 1),
            },
            "Anaire", "Aranwe", "Aredhel", "Caranthir", "Celebrian", "Celegorm", "Cirdan", "Curufin", "Earwen", "Edrahil", "Elenwe", "Elladan", "Enel", "Enelye", "Enerdhil", "Erestor", "Feanor", "Findis", "Finduilas", "Fingolfin", "Fingon", "Galathil", "Gelmir", "Glorfindel", "Guilin", "Hendor", "Idril", "Imin", "Iminye", "Indis", "Ingwe", "Ingwion", "Lenwe", "Lindir", "Maeglin", "Mahtan", "Miriel", "Mithrellas", "Nellas", "Nerdanel", "Nimloth", "Oropher", "Orophin", "Saeros", "Salgant", "Tatie", "Thingol", "Turgon", "Vaire") {
        @Override
        public List<Item> generateRandomLoot(final boolean doubleLoot) {
            if (Utils.randomBoolean(34)) {
                return List.of(new Item(ItemId.CRYSTAL_SHARD, doubleLoot ? 2 : 1));
            }
            if (Utils.randomBoolean(1023)) {
                return List.of(new Item(ItemId.ENHANCED_CRYSTAL_TELEPORT_SEED, doubleLoot ? 2 : 1));
            }
            return super.generateRandomLoot(doubleLoot);
        }
    },
    TZHAAR_HUR(90, 90, 103.5, true, 5, 4, 4, new String[]{"What do you think you're doing?"}, new ImmutableItem[]{new ImmutableItem(6529, 3, 14), new ImmutableItem(1623), new ImmutableItem(1621), new ImmutableItem(1619), new ImmutableItem(1617)}, "TzHaar-Hur"),
    ;

    private final CoinPouch coinPouch;

    PocketData(final int level, final int successLevel, final double experience, final boolean randomLoot, final int stunTime, final int minDamage, final int maxDamage, final String[] possibleMessages, final ImmutableItem[] loot, final Object... npcs) {
        this(level, successLevel, experience, null, randomLoot, stunTime, minDamage, maxDamage, possibleMessages, loot, npcs);
    }

    public static final Map<Integer, PocketData> DATA_BY_ID = new HashMap<>();
    public static final Map<String, PocketData> DATA_BY_NAME = new HashMap<>();

    static {
        for (final PocketData data : values()) {
            for (final Object o : data.npcs) {
                if (o instanceof Integer) DATA_BY_ID.put((Integer) o, data);
                else if (o instanceof String) DATA_BY_NAME.put((String) o, data);
            }
        }
    }

    /**
     * Gets the pocket data for the given NPC. Prioritises id above the name.
     *
     * @param npc whose data to get.
     * @return data
     */
    public static PocketData getData(@NotNull final Player player, @NotNull final NPC npc) {
        final PocketData id = DATA_BY_ID.get(npc.getId());
        if (id == null) return DATA_BY_NAME.get(npc.getName(player));
        return id;
    }

    public List<Item> generateRandomLoot(final boolean doubleLoot) {
        if (isRandomLoot()) {
            final ImmutableItem random = getLoot()[Utils.random(getLoot().length - 1)];
            final int baseMultiplier =  /*this == MASTER_FARMER ? 3 : */ 1;
            return generateLootItems(random, doubleLoot, baseMultiplier);
        } else {
            final int roll = Utils.random(totalWeight);
            int current = 0;
            for (final ImmutableItem loot : this.loot) {
                if ((current += loot.getRate()) >= roll) {
                    final int multiplier = loot.getId() == 995 ? 5 : 1;
                    return generateLootItems(loot, doubleLoot, multiplier);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<Item> generateLootItems(final ImmutableItem item, final boolean doubleLoot, final int baseMultiplier) {
        final ArrayList<Item> rewards = new ArrayList<>();
        final boolean isPouch = CoinPouch.ITEMS.containsKey(item.getId());
        final int baseAmount = item.generateResult().getAmount();
        final int bonusMultiplier = (doubleLoot && !isPouch ? 2 : 1);
        rewards.add(new Item(item.getId(), baseAmount * bonusMultiplier * baseMultiplier));
        if (isPouch && doubleLoot) {
            final CoinPouch pouch = CoinPouch.ITEMS.get(item.getId());
            rewards.add(new Item(ItemId.COINS_995, com.zenyte.plugins.item.CoinPouch.getCoinAmount(pouch, 1)));
        }
        return rewards;
    }

    private final int totalWeight;
    private final int level;
    private final int stunTime;
    private final int minDamage;
    private final int maxDamage;
    private final double experience;
    private final boolean randomLoot;
    private final String[] possibleMessages;
    private final ImmutableItem[] loot;
    private final Object[] npcs;
    private final int successLevel;

    PocketData(final int level, final int successLevel, final double experience, final CoinPouch coinPouch, final boolean randomLoot, final int stunTime, final int minDamage, final int maxDamage, final String[] possibleMessages, final ImmutableItem[] loot, final Object... npcs) {
        this.level = level;
        this.successLevel = successLevel;
        this.experience = experience;
        this.coinPouch = coinPouch;
        this.randomLoot = randomLoot;
        this.stunTime = stunTime;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.possibleMessages = possibleMessages;
        this.loot = loot;
        this.npcs = npcs;
        int weight = 0;
        if (!randomLoot) {
            for (final ImmutableItem item : loot) {
                weight += (int) item.getRate();
            }
        }
        this.totalWeight = weight;
    }

    public CoinPouch getCoinPouch() {
        return coinPouch;
    }

    public int getLevel() {
        return level;
    }

    public int getStunTime() {
        return stunTime;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public double getExperience() {
        return experience;
    }

    public boolean isRandomLoot() {
        return randomLoot;
    }

    public String[] getPossibleMessages() {
        return possibleMessages;
    }

    public ImmutableItem[] getLoot() {
        return loot;
    }

    public Object[] getNpcs() {
        return npcs;
    }

    public int getSuccessLevel() {
        return successLevel;
    }
}
