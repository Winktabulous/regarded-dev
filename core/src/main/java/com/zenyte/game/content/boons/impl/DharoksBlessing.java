package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class DharoksBlessing extends Boon {

    @Override
    public String name() {
        return "Dharok's Blessing";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_DharoksBlessing;
    }

    @Override
    public String description() {
        return "Provides a 10% damage boost when fighting under 10 hitpoints.";
    }

    @Override
    public int item() {
        return 4716;
    }
}
