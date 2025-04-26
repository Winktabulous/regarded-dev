package com.zenyte.game.content.minigame.blastfurnace;

import com.zenyte.game.content.skills.smithing.SmeltableBar;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public enum BlastFurnaceVarbit {
    ADAMANTITE_ORE(953, VarbitType.ORE, BlastFurnaceOre.ADAMANTITE_ORE), ADAMANT_BAR(945, VarbitType.BAR, SmeltableBar.ADAMANTITE_BAR), BRONZE_BAR(941, VarbitType.BAR, SmeltableBar.BRONZE_BAR), COAL(949, VarbitType.ORE, BlastFurnaceOre.COAL), COPPER_ORE(959, VarbitType.ORE, BlastFurnaceOre.COPPER_ORE), GOLD_BAR(947, VarbitType.BAR, SmeltableBar.GOLD_BAR), GOLD_ORE(955, VarbitType.ORE, BlastFurnaceOre.GOLD_ORE), IRON_BAR(942, VarbitType.BAR, SmeltableBar.IRON_BAR), IRON_ORE(951, VarbitType.ORE, BlastFurnaceOre.IRON_ORE), MITHRIL_BAR(944, VarbitType.BAR, SmeltableBar.MITHRIL_BAR), MITHRIL_ORE(952, VarbitType.ORE, BlastFurnaceOre.MITHRIL_ORE), RUNE_BAR(946, VarbitType.BAR, SmeltableBar.RUNITE_BAR), RUNITE_ORE(954, VarbitType.ORE, BlastFurnaceOre.RUNITE_ORE), SILVER_BAR(948, VarbitType.BAR, SmeltableBar.SILVER_BAR), SILVER_ORE(956, VarbitType.ORE, BlastFurnaceOre.SILVER_ORE), STEEL_BAR(943, VarbitType.BAR, SmeltableBar.STEEL_BAR), TIN_ORE(950, VarbitType.ORE, BlastFurnaceOre.TIN_ORE);
    public static final Map<SmeltableBar, BlastFurnaceVarbit> barVarbits = new HashMap<>();
    public static final Map<BlastFurnaceOre, BlastFurnaceVarbit> oreVarbits = new HashMap<>();
    public static final BlastFurnaceVarbit[] VALUES = values();

    static {
        for (final BlastFurnaceVarbit varbit : VALUES) {
            if (varbit.type.equals(VarbitType.BAR)) {
                final SmeltableBar bar = (SmeltableBar) varbit.anchor;
                barVarbits.put(bar, varbit);
            } else if (varbit.type.equals(VarbitType.ORE)) {
                final BlastFurnaceOre ore = (BlastFurnaceOre) varbit.anchor;
                oreVarbits.put(ore, varbit);
            }
        }
    }

    private final int varbit;
    private final VarbitType type;
    private final Object anchor;

    BlastFurnaceVarbit(final int varbit, final VarbitType type, final Object anchor) {
        this.varbit = varbit;
        this.type = type;
        this.anchor = anchor;
    }

    public static BlastFurnaceVarbit getData(final BlastFurnaceOre anchor) {
        return oreVarbits.get(anchor);
    }

    public static BlastFurnaceVarbit getData(final SmeltableBar anchor) {
        return barVarbits.get(anchor);
    }


    private enum VarbitType {
        ORE, BAR
    }

    public static Map<SmeltableBar, BlastFurnaceVarbit> getBarVarbits() {
        return barVarbits;
    }

    public static Map<BlastFurnaceOre, BlastFurnaceVarbit> getOreVarbits() {
        return oreVarbits;
    }

    public int getVarbit() {
        return varbit;
    }

    public VarbitType getType() {
        return type;
    }
}
