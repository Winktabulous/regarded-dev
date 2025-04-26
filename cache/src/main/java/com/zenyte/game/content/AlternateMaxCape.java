package com.zenyte.game.content;

import com.zenyte.game.item.ItemId;

import java.util.HashMap;
import java.util.Map;

public enum AlternateMaxCape {
    ASSEMBLER_TO_MASORI(ItemId.MASORI_ASSEMBLER_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_HOOD, ItemId.ASSEMBLER_MAX_CAPE, ItemId.ASSEMBLER_MAX_HOOD, ItemId.MASORI_CRAFTING_KIT)
    ;

    private final int alternateCape;
    private final int alternateHood;
    private final int cape;
    private final int hood;
    private final int upgrade;
    public static final AlternateMaxCape[] values = values();
    private static final Map<Integer, AlternateMaxCape> CAPES = new HashMap<>(values.length);

    public static AlternateMaxCape get(final int value) {
        return CAPES.get(value);
    }

    static {
        for (final AlternateMaxCape cape : values) {
            CAPES.put(cape.getUpgrade(), cape);
        }
    }

    AlternateMaxCape(int cape, int hood, int alternateBase, int alternateHood, int upgrade) {
        this.cape = cape;
        this.hood = hood;
        this.alternateCape = alternateBase;
        this.alternateHood = alternateHood;
        this.upgrade = upgrade;
    }

    public int getCape() {
        return cape;
    }

    public int getHood() {
        return hood;
    }

    public int getUpgrade() {
        return upgrade;
    }
    public int getAlternateCape() {
        return alternateCape;
    }
    public int getAlternateHood() {
        return alternateHood;
    }
}
