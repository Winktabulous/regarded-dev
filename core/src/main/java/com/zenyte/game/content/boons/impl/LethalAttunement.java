package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class LethalAttunement extends Boon {
    @Override
    public String name() {
        return "Lethal Attunement";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_LethalAttunement;
    }

    @Override
    public String description() {
        return "Provides the ability that revenant weapons no longer require ether charges to use.";
    }

    @Override
    public int item() {
        return 22547;
    }
}
