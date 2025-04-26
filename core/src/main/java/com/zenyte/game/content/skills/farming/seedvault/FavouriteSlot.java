package com.zenyte.game.content.skills.farming.seedvault;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.utils.StaticInitializer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@StaticInitializer

public enum FavouriteSlot {
    FIRST(1, 8172),
    SECOND(2, 8173),
    THIRD(3, 8174),
    FOURTH(4, 8175),
    FIFTH(5, 8176),
    SIXTH(6, 8177),
    SEVENTH(7, 8178),
    EIGHT(8, 8179);
    private static final FavouriteSlot[] slots = values();

    static {
        for (FavouriteSlot slot : slots) {
            VarManager.appendPersistentVarbit(slot.getVarbit());
        }
    }

    private static final String seedVaultInitializationKey = "seed vault initialized";

    private final int slot;
    private final int varbit;

    public static Optional<FavouriteSlot> getBySeedSlot(final Player player, final int seedSlot) {
        for (FavouriteSlot slot : slots) {
            if (player.getVarManager().getBitValue(slot.getVarbit()) == seedSlot) {
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }

    public static Optional<FavouriteSlot> getFreeSlot(final Player player) {
        for (FavouriteSlot slot : slots) {
            if (player.getVarManager().getBitValue(slot.getVarbit()) == 255) {
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if the first two (any two slots will work however) favourite slot varbit values for player are 0.
     * This should only happen on first time login after seed vault release. The reason for this is by default varbit
     * values are set to 0 and instead should be 255 for favourite slots.
     */
    public static boolean isSet(final Player player) {
        return player.getNumericAttribute(seedVaultInitializationKey).intValue() != 0;
    }

    public static void setInitialized(@NotNull final Player player) {
        player.addAttribute(seedVaultInitializationKey, 1);
    }

    public static void reset(final Player player) {
        for (FavouriteSlot slot : slots) {
            player.getVarManager().sendBit(slot.getVarbit(), 255);
        }
    }

    FavouriteSlot(int slot, int varbit) {
        this.slot = slot;
        this.varbit = varbit;
    }

    public int getSlot() {
        return slot;
    }

    public int getVarbit() {
        return varbit;
    }
}
