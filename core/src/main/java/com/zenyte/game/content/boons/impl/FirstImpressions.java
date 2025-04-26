package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class FirstImpressions extends Boon {
    @Override
    public String name() {
        return "First IMPressions";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_FirstImpressions;
    }

    @Override
    public String description() {
        return "Provides a 100% increase to loot from Impling Jars.";
    }

    @Override
    public int item() {
        return 19732;
    }
}
