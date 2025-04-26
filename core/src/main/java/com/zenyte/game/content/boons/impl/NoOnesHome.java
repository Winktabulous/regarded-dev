package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class NoOnesHome extends Boon {
    @Override
    public String name() {
        return "No One's Home";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_NoOnesHome;
    }

    @Override
    public String description() {
        return "Provides a 100% increase in XP at the AFK Zone";
    }

    @Override
    public int item() {
        return 2528;
    }
}
