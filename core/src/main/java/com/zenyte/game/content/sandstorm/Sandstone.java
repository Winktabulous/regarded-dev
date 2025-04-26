package com.zenyte.game.content.sandstorm;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.IntListUtils;
import it.unimi.dsi.fastutil.ints.IntLists;

import java.util.EnumSet;

/**
 * @author Chris
 * @since August 20 2020
 */

public enum Sandstone {
    ONE_KG(ItemId.SANDSTONE_1KG, 1),
    TWO_KG(ItemId.SANDSTONE_2KG, 2),
    FIVE_KG(ItemId.SANDSTONE_5KG, 4),
    TEN_KG(ItemId.SANDSTONE_10KG, 8);

    public static final ImmutableSet<Sandstone> SANDSTONES = Sets.immutableEnumSet(EnumSet.allOf(Sandstone.class));
    public static final IntLists.UnmodifiableList SANDSTONE_IDS = IntListUtils.unmodifiable(SANDSTONES.stream().mapToInt(Sandstone::getItemId).toArray());
    private final int itemId;
    private final int bucketsOfSand;

    Sandstone(int itemId, int bucketsOfSand) {
        this.itemId = itemId;
        this.bucketsOfSand = bucketsOfSand;
    }

    public int getItemId() {
        return itemId;
    }

    public int getBucketsOfSand() {
        return bucketsOfSand;
    }
}
