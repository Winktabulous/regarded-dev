package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class NoPetDebt extends Boon {
    @Override
    public String name() {
        return "No Pet Debt";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_NoPetDebt;
    }

    @Override
    public String description() {
        return "Provides a passive 100% increase to pet drop rates.";
    }

    @Override
    public int item() {
        return 12646;
    }
}
