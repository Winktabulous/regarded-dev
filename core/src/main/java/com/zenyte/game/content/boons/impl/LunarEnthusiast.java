package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class LunarEnthusiast extends Boon {

    @Override
    public String name() {
        return "Lunar Enthusiast";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_LunarEnthusiast;
    }

    @Override
    public String description() {
        return "Provides a boost that doubles vengeance damage. (150%)";
    }

    @Override
    public int item() {
        return 9084;
    }
}
