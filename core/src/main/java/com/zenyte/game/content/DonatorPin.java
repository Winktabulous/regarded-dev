package com.zenyte.game.content;

import com.near_reality.game.item.CustomItemId;

public enum DonatorPin {

    DONATOR_PIN_10(CustomItemId.DONATOR_PIN_10, 75_000_000),
    DONATOR_PIN_25(CustomItemId.DONATOR_PIN_25, 187_500_000),
    DONATOR_PIN_50(CustomItemId.DONATOR_PIN_50, 375_000_000),
    DONATOR_PIN_100(CustomItemId.DONATOR_PIN_100, 750_000_000),
    ;

    private int itemId;
    private int value;

    public int getItemId() {
        return itemId;
    }

    public int getValue() {
        return value;
    }

    DonatorPin(int itemId, int value) {
        this.itemId = itemId;
        this.value = value;
    }

    public static DonatorPin forId(int id) {
        for (DonatorPin value : VALUES) {
            if(value.itemId == id)
                return value;
        }
        return null;
    }

    public static DonatorPin[] VALUES = values();
}
