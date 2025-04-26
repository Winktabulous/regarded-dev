package com.zenyte.game.content.minigame.blastfurnace;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public enum BlastFurnaceOre {

    // coal technically has a "barId" of a steel bar, to make sorting this data linear and straightforward
    COAL(453, 2932, 2353),
    TIN_ORE(438, 2924, 2349),
    COPPER_ORE(436, 2925, 2349),
    IRON_ORE(440, 2926, 2351),
    SILVER_ORE(442, 2930, 2355),
    GOLD_ORE(444, 2931, 2357),
    MITHRIL_ORE(447, 2927, 2359),
    ADAMANTITE_ORE(449, 2928, 2361),
    RUNITE_ORE(451, 2929, 2363),

    ;

    private final int itemId;
    private final int npcId;
    private final int barId;

    public static final BlastFurnaceOre[] VALUES = values();
    public static final IntList ORE_IDS = new IntArrayList();
    public static final List<Integer> ORE_NPCS = new ArrayList<>();

    public static final Map<Integer, BlastFurnaceOre> map = new HashMap<>();

    public static int[] ORE_IDS_ARRAY;

    public static BlastFurnaceOre getOre(final int id) {
        return map.get(id) == null ? COAL : map.get(id);
    }
    
    public boolean isPrimaryOre() {
        return this != TIN_ORE && this != COAL;
    }
    
    public int getMaxAmt() {
        return this == TIN_ORE || this == COAL ? 254 : 28;
    }

    static {
        for (BlastFurnaceOre ore : VALUES) {
            ORE_IDS.add(ore.getItemId());
            ORE_NPCS.add(ore.getNpcId());

            map.put(ore.getItemId(), ore);
        }

        ORE_IDS_ARRAY = ORE_IDS.toIntArray();
    }

    BlastFurnaceOre(int itemId, int npcId, int barId) {
        this.itemId = itemId;
        this.npcId = npcId;
        this.barId = barId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getBarId() {
        return barId;
    }

}
