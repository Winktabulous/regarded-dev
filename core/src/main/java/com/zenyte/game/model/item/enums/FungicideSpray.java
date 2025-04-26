package com.zenyte.game.model.item.enums;

import com.zenyte.game.world.entity.player.Player;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommeh | 23-3-2019 | 19:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum FungicideSpray {
    EMPTY(7431, null), CHARGE_1(7430, EMPTY), CHARGE_2(7429, CHARGE_1), CHARGE_3(7428, CHARGE_2), CHARGE_4(7427, CHARGE_3), CHARGE_5(7426, CHARGE_4), CHARGE_6(7425, CHARGE_5), CHARGE_7(7424, CHARGE_6), CHARGE_8(7423, CHARGE_7), CHARGE_9(7422, CHARGE_8), CHARGE_10(7421, CHARGE_9);
    private final int id;
    private final FungicideSpray nextCharge;
    public static final Set<FungicideSpray> ALL = EnumSet.allOf(FungicideSpray.class);
    private static final Map<Integer, FungicideSpray> SPRAYS = new HashMap<>();

    public static FungicideSpray get(final int id) {
        return SPRAYS.get(id);
    }

    public static Object[] get(final Player player) {
        for (final FungicideSpray spray : ALL) {
            if (spray.equals(EMPTY)) {
                continue;
            }
            if (player.getInventory().containsItem(spray.getId(), 1)) {
                final int slot = player.getInventory().getContainer().getSlotOf(spray.getId());
                return new Object[] {spray, slot};
            }
        }
        return null;
    }

    static {
        for (final FungicideSpray spray : ALL) {
            SPRAYS.put(spray.getId(), spray);
        }
    }

    FungicideSpray(int id, FungicideSpray nextCharge) {
        this.id = id;
        this.nextCharge = nextCharge;
    }

    public int getId() {
        return id;
    }

    public FungicideSpray getNextCharge() {
        return nextCharge;
    }
}
