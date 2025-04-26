package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class MasterOfTheCraft extends Boon {

    @Override
    public String name() {
        return "Master of the Craft";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_MasterOfTheCraft;
    }

    @Override
    public String description() {
        return "Provides a 2x speed boost when smithing, fletching, or gem cutting.";
    }

    @Override
    public int item() {
        return 1631;
    }
}
