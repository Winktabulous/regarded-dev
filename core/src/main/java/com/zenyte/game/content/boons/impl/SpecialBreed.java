package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class SpecialBreed extends Boon {
    @Override
    public String name() {
        return "Special Breed";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SpecialBreed;
    }

    @Override
    public String description() {
        return "Provides a 10% increase in special attack energy regen speed.";
    }

    @Override
    public int item() {
        return 25975;
    }
}
