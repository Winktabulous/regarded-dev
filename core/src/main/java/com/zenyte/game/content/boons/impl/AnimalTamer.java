package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class AnimalTamer extends Boon {
    @Override
    public String name() {
        return "Animal Tamer";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_AnimalTamer;
    }

    @Override
    public String description() {
        return "Provides a 2% drop rate boost when a pet is following you.";
    }

    @Override
    public int item() {
        return 10150;
    }
}
