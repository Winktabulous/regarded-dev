package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class SlayersSpite extends Boon {
    @Override
    public String name() {
        return "Slayer's Spite";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SlayersSpite;
    }

    @Override
    public String description() {
        return "Provides a 5% damage and accuracy boost when wearing a Slayer Helmet.";
    }

    @Override
    public int item() {
        return 11864;
    }
}
