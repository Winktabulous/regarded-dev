package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class AshesToAshes extends Boon {
    @Override
    public String name() {
        return "Ashes to Ashes";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_AshesToAshes;
    }

    @Override
    public String description() {
        return "Provides a 300% passive xp boost when using the Ash Sanctifier";
    }

    @Override
    public int item() {
        return ItemId.ASH_SANCTIFIER;
    }
}
