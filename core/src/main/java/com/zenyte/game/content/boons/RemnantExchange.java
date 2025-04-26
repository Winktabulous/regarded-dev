package com.zenyte.game.content.boons;

import com.zenyte.game.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemnantExchange {
    public static boolean eligibleItem(@NotNull Item itemInSlot) {
        return RemnantValueManager.isPresent(itemInSlot.getId());
    }

    public static boolean ineligibleItem(@NotNull Item itemInSlot) {
        return !RemnantValueManager.isPresent(itemInSlot.getId());
    }

    public static int getValueForItem(@NotNull Item itemInSlot) {
        return RemnantValueManager.getValue(itemInSlot);
    }
}
