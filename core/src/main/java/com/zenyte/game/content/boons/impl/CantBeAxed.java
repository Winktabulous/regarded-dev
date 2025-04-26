package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class CantBeAxed extends Boon {
    @Override
    public String name() {
        return "Can't Be Axed";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_CantBeAxed;
    }

    @Override
    public String description() {
        return "Vardorvis's axes no longer cause bleed and deal 33% less impact damage";
    }

    @Override
    public int item() {
        return ItemId.VARDORVIS_MEDALLION;
    }
}
