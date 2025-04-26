package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class SousChef extends Boon {
    @Override
    public String name() {
        return "Sous Chef";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SousChef;
    }

    @Override
    public String description() {
        return "Provides a 100% increase to cooking speed and prevents burning.";
    }

    @Override
    public int item() {
        return 7441;
    }
}
