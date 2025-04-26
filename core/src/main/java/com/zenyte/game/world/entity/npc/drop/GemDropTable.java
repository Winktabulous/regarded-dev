package com.zenyte.game.world.entity.npc.drop;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * @author Kris | 03/04/2019 22:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum GemDropTable {
    SAPPHIRE(32, new Item(1623)), EMERALD(16, new Item(1621)), RUBY(8, new Item(1619)), TALISMAN(3, null), DIAMOND(2, new Item(1617)), RUNE_JAVELIN(1, new Item(830, 5)), LOOP_HALF_OF_A_KEY(1, new Item(987)), TOOTH_HALF_OF_A_KEY(1, new Item(985));
    private final int weight;
    private final Item item;
    private static final GemDropTable[] values = values();
    private static final int TOTAL_WEIGHT;

    static {
        int weight = 0;
        for (final GemDropTable value : values) {
            weight += value.weight;
        }
        TOTAL_WEIGHT = weight;
    }

    public static final Optional<Item> get(@NotNull final Player player) {
        final Item ring = player.getRing();
        final boolean row = ring != null && ring.getName().startsWith("Ring of wealth");
        final int maxRoll = row ? TOTAL_WEIGHT : 127;
        final int roll = Utils.random(maxRoll);
        if (roll == maxRoll) {
            return MegaRareDropTable.get(player);
        }
        int currentRoll = 0;
        for (final GemDropTable value : values) {
            if ((currentRoll += value.weight) >= roll) {
                if (value == TALISMAN) {
                    final Location loc = player.getLocation();
                    final int prx = loc.getRegionX();
                    final int pry = loc.getRegionY();
                    return Optional.of(new Item(prx >= 32 && prx <= 60 && pry >= 39 && pry <= 63 ? 1462 : 1452));
                }
                return Optional.of(new Item(value.item));
            }
        }
        return Optional.empty();
    }

    public static final boolean isGDTNPC(@NotNull final String name) {
        return GDTNPC.map.containsKey(name.toLowerCase());
    }

    public static final GDTNPC getTable(final int id) {
        return GDTNPC.map.get(NPCDefinitions.getOrThrow(id).getName().toLowerCase());
    }

    public static final boolean roll(@NotNull final NPC npc) {
        final GemDropTable.GDTNPC gdtnpc = GDTNPC.map.get(npc.getDefinitions().getName().toLowerCase());
        if (gdtnpc != null) {
            return Utils.random(gdtnpc.maxRoll - 1) < gdtnpc.roll;
        }
        return false;
    }

    GemDropTable(int weight, Item item) {
        this.weight = weight;
        this.item = item;
    }

    public int getWeight() {
        return weight;
    }

    public Item getItem() {
        return item;
    }


    public enum GDTNPC {
        GORAK("Gorak", 1, 1), ABHORRENT_SPECTRE("Abhorrent spectre", 5, 128), SHADOW_WARRIOR("Shadow warrior", 1, 16), ICE_TROLL_RUNT("Ice troll runt", 5, 128), GARGOYLE("Gargoyle", 5, 128), ABERRANT_SPECTRE("Aberrant spectre", 5, 128), STEEL_DRAGON("Steel dragon", 1, 32), COMMANDER_ZILYANA("Commander Zilyana", 2, 127), SAND_CRAB("Sand crab", 2, 128), HOBGOBLIN("Hobgoblin", 2, 128), MINOTAUR("Minotaur", 1, 128), ABYSSAL_DEMON("Abyssal demon", 5, 128), BLACK_DEMON("Black demon", 5, 128), BLACK_DRAGON("Black dragon", 3, 128), BRONZE_DRAGON("Bronze dragon", 4, 128), BRUTAL_BLACK_DRAGON("Brutal black dragon", 3, 128), BRUTAL_GREEN_DRAGON("Brutal green dragon", 2, 128), CRAZY_ARCHAEOLOGIST("Crazy archaeologist", 4, 128), DARK_BEAST("Dark beast", 3, 128), GIANT_MOLE("Giant mole", 5, 128), GREATER_ABYSSAL_DEMON("Greater abyssal demon", 5, 128), INSATIABLE_MUTATED_BLOODVELD("Insatiable mutated bloodveld", 2, 128), IRON_DRAGON("Iron dragon", 3, 128), LAVA_DRAGON("Lava dragon", 5, 128), MITHRIL_DRAGON("Mithril dragon", 4, 128), NECHRYAEL("Nechryael", 5, 116), NIGHT_BEAST("Night beast", 3, 128), NUCLEAR_SMOKE_DEVIL("Nuclear smoke devil", 4, 128), SCORPIA("Scorpia", 4, 128), SMOKE_DEVIL("Smoke devil", 4, 128), WYRM("Wyrm", 1, 76), DRAKE("Drake", 1, 85), HYDRA("Hydra", 1, 128), SULPHUR_LIZARD("Sulphur Lizard", 6, 128);
        private final String name;
        private final int roll;
        private final int maxRoll;
        private static final GDTNPC[] values = values();
        private static final Map<String, GDTNPC> map = new Object2ObjectOpenHashMap<>(values.length);

        static {
            for (final GemDropTable.GDTNPC value : values) {
                map.put(value.name.toLowerCase(), value);
            }
        }

        GDTNPC(String name, int roll, int maxRoll) {
            this.name = name;
            this.roll = roll;
            this.maxRoll = maxRoll;
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
    }
}
