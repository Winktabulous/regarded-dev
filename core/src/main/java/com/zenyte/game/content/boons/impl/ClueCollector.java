package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class ClueCollector extends Boon {
    @Override
    public String name() {
        return "Clue Collector";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_ClueCollector;
    }

    @Override
    public String description() {
        return "Provides an passive effect that causes all dropped clues to go to your inventory";
    }

    @Override
    public int item() {
        return 22675;
    }
}
