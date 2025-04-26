package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class IgnoranceIsBliss extends Boon {
    @Override
    public String name() {
        return "Ignorance Is Bliss";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_IgnoranceIsBliss;
    }

    @Override
    public String description() {
        return "Provides a passive effect to remove Phantom Muspah's prayer shield";
    }

    @Override
    public int item() {
        return ItemId.MUPHIN;
    }
}
