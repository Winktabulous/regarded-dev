package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class CrushingBlow extends Boon {

    @Override
    public String name() {
        return "Crushing Blow";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_CrushingBlow;
    }

    @Override
    public String description() {
        return "Provides a passive effect that causes crush attacks to ignore 10% of enemy defence.";
    }

    @Override
    public int item() {
        return 24417;
    }
}
