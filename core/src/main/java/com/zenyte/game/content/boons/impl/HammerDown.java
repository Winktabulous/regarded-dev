package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class HammerDown extends Boon {
    @Override
    public String name() {
        return "Hammer Down";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HammerDown;
    }

    @Override
    public String description() {
        return "Provides a 15% damage boost to the DWH spec. with a min damage of 5.";
    }

    @Override
    public int item() {
        return 13576;
    }
}
