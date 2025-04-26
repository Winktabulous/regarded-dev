package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class RevItUp extends Boon {
    @Override
    public String name() {
        return "Rev' it up";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_RevItUp;
    }

    @Override
    public String description() {
        return "Provides a 2x boost to coins and ether dropped by revenants.";
    }

    @Override
    public int item() {
        return 21820;
    }
}
