package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class CrystalCatalyst extends Boon {
    @Override
    public String name() {
        return "Crystal Catalyst";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_CrystalCatalyst;
    }

    @Override
    public String description() {
        return "Provides an additional roll when completing the Gauntlet.";
    }

    @Override
    public int item() {
        return 25862;
    }
}
