package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class Woodsman extends Boon {
    @Override
    public String name() {
        return "Woodsman";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_Woodsman;
    }

    @Override
    public String description() {
        return "Provides a 100% increase to woodcutting speed and raises bird nest rates.";
    }

    @Override
    public int item() {
        return 6739;
    }
}
