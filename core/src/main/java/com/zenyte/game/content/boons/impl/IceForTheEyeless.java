package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class IceForTheEyeless extends Boon {
    @Override
    public String name() {
        return "Ice for the Eyeless";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_IceForTheEyeless;
    }

    @Override
    public String description() {
        return "Provides a 10% damage boost to krakens when using ice spells.";
    }

    @Override
    public int item() {
        return 20617;
    }
}
