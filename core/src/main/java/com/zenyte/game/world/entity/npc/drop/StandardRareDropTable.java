package com.zenyte.game.world.entity.npc.drop;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ImmutableItem;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * @author Kris | 04/04/2019 00:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum StandardRareDropTable {
    NATURE_RUNE(3, new ImmutableItem(561, 62, 67)), LAW_RUNE(2, new ImmutableItem(563, 45)), DEATH_RUNE(2, new ImmutableItem(560, 45)), STEEL_ARROW(2, new ImmutableItem(886, 150)), RUNE_ARROW(2, new ImmutableItem(892, 42)), ADAMANT_JAVELIN(2, new ImmutableItem(829, 5)), RUNE_2H_SWORD(3, new ImmutableItem(1319)), RUNE_BATTLEAXE(3, new ImmutableItem(1373)), RUNE_SQ_SHIELD(2, new ImmutableItem(1185)), RUNE_KITESHIELD(1, new ImmutableItem(1201)), DRAGON_MED_HELM(1, new ImmutableItem(1149)), COINS(22, new ImmutableItem(995, 3000)), RUNITE_BAR(5, new ImmutableItem(2363)), DRAGONSTONE(2, new ImmutableItem(1615)), SILVER_ORE(2, new ImmutableItem(443, 100));
    private final int weight;
    private final ImmutableItem item;
    private static final StandardRareDropTable[] values = values();
    private static final int TOTAL_WEIGHT;

    static {
        int weight = 0;
        for (final StandardRareDropTable value : values) {
            weight += value.weight;
        }
        TOTAL_WEIGHT = weight;
    }

    public static final Optional<Item> get(@NotNull final Player player) {
        final int roll = Utils.random(127);
        if (roll >= 107) {
            return GemDropTable.get(player);
        }
        if (roll >= 92) {
            return MegaRareDropTable.get(player);
        }
        int currentRoll = 0;
        for (final StandardRareDropTable value : values) {
            if ((currentRoll += value.weight) >= roll) {
                return Optional.of(new Item(value.item.getId(), Utils.random(value.item.getMinAmount(), value.item.getMaxAmount())));
            }
        }
        return Optional.empty();
    }

    public static final boolean isSRDTNPC(@NotNull final String name) {
        return SRDTNPC.map.containsKey(name.toLowerCase());
    }

    public static final boolean roll(@NotNull final NPC npc) {
        final StandardRareDropTable.SRDTNPC srdtnpc = SRDTNPC.map.get(npc.getDefinitions().getName().toLowerCase());
        if (srdtnpc != null) {
            return Utils.random(srdtnpc.maxRoll - 1) < srdtnpc.roll;
        }
        return false;
    }

    public static final SRDTNPC getTable(final int id) {
        return SRDTNPC.map.get(NPCDefinitions.getOrThrow(id).getName().toLowerCase());
    }

    StandardRareDropTable(int weight, ImmutableItem item) {
        this.weight = weight;
        this.item = item;
    }

    public int getWeight() {
        return weight;
    }

    public ImmutableItem getItem() {
        return item;
    }


    public enum SRDTNPC {
        KREE_ARRA("Kree'arra", 8, 127), COMMANDER_ZILYANA("Commander Zilyana", 8, 127), GENERAL_GRAARDOR("General Graardor", 8, 127), KRIL_TSUTSAROTH("K'ril Tsutsaroth", 8, 127), KING_BLACK_DRAGON("King black dragon", 1, 16),  LIZARDMAN_SHAMAN("Lizardman shaman", 1, 25), MUTATED_BLOODVELD("Mutated bloodveld", 1, 32), FIRE_GIANT("Fire giant", 1, 128), ABYSSAL_DEMON("Abyssal demon", 2, 128), ABYSSAL_SIRE("Abyssal sire", 3, 139, true, 1), ADAMANT_DRAGON("Adamant dragon", 1, 99), ALCHEMICAL_HYDRA("Alchemidal hydra", 1, 101, false, 2), ANCIENT_WYVERN("Ancient wyvern", 1, 128), BLACK_DEMON("Black demon", 1, 128), BLACK_DRAGON("Black dragon", 2, 128), BRONZE_DRAGON("Bronze dragon", 1, 128), BRUTAL_BLACK_DRAGON("Brutal black dragon", 2, 128), BRUTAL_GREEN_DRAGON("Brutal green dragon", 3, 128), CRAZY_ARCHAEOLOGIST("Crazy archaeologist", 4, 128), CYCLOPS("Cyclops", 1, 128), DARK_BEAST("Dark beast", 3, 128), DEMONIC_GORILLA("Demonic gorilla", 5, 500), DERANGED_ARCHAEOLOGIST("Deranged archaeologist", 2, 128), ELDER_CHAOS_DRUID("Elder chaos druid", 3, 128), GIANT_MOLE("Giant mole", 4, 128), GREATER_ABYSSAL_DEMON("Greater abyssal demon", 2, 128), INSATIABLE_MUTATED_BLOODVELD("Insatiable mutated bloodveld", 3, 128), IRON_DRAGON("Iron dragon", 2, 128), KALPHITE_GUARDIAN("Kalphite guardian", 1, 128), KALPHITE_QUEEN("Kalphite queen", 1, 128), LAVA_DRAGON("Lava dragon", 3, 128), MARBLE_GARGOYLE("Marble gargoyle", 3, 128), MITHRIL_DRAGON("Mithril dragon", 3, 128), NECHRYAEL("Nechryael", 1, 116), NECHRYARCH("Nechryarch", 3, 116), NIGHT_BEAST("Night beast", 3, 128), NUCLEAR_SMOKE_DEVIL("Nuclear smoke devil", 4, 128), RUNE_DRAGON("Rune dragon", 1, 127), SCORPIA("Scorpia", 4, 128), SCREAMING_TWISTED_BANSHEE("Screaming twisted banshee", 2, 128), SKELETAL_WYVERN("Skeletal wyvern", 3, 128), SKELETON("Skeleton", 1, 500), SMOKE_DEVIL("Smoke devil", 4, 128), STEEL_DRAGON("Steel dragon", 4, 128), TORTURED_SOUL("Tortured soul", 1, 128), TWISTED_BANSHEE("Twisted banshee", 1, 128), VORKATH("Vorkath", 5, 150), VYREWATCH("Vyrewatch", 1, 128);
        private final String name;
        private final int roll;
        private final int maxRoll;
        private final int rollsCount;
        private final boolean doubleLoot;

        SRDTNPC(final String name, final int roll, final int maxRoll) {
            this(name, roll, maxRoll, false, 1);
        }

        SRDTNPC(final String name, final int roll, final int maxRoll, final boolean doubleLoot, final int rollsCount) {
            this.name = name;
            this.roll = roll;
            this.maxRoll = maxRoll;
            this.doubleLoot = doubleLoot;
            this.rollsCount = rollsCount;
        }

        private static final SRDTNPC[] values = values();
        private static final Map<String, SRDTNPC> map = new Object2ObjectOpenHashMap<>(values.length);

        static {
            for (final StandardRareDropTable.SRDTNPC value : values) {
                map.put(value.name.toLowerCase(), value);
            }
        }

        public String getName() {
            return name;
        }

        public int getRoll() {
            return roll;
        }

        public int getMaxRoll() {
            return maxRoll;
        }

        public int getRollsCount() {
            return rollsCount;
        }

        public boolean isDoubleLoot() {
            return doubleLoot;
        }
    }
}
