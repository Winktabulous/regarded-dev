package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class Botanist extends Boon {
    @Override
    public String name() {
        return "Botanist";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_Botanist;
    }

    @Override
    public String description() {
        return "Provides double yield from herb patches and notes gathered herbs.";
    }

    @Override
    public int item() {
        return 5329;
    }
}
