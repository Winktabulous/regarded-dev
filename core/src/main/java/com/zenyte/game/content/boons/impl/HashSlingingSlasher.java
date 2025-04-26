package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class HashSlingingSlasher extends Boon {
    @Override
    public String name() {
        return "Hash Slinging Slasher";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HashSlingingSlasher;
    }

    @Override
    public String description() {
        return "Using a slash weapon ignores 10% of the enemy target's defense (PVM).";
    }

    @Override
    public int item() {
        return ItemId.VESTAS_LONGSWORD;
    }
}
