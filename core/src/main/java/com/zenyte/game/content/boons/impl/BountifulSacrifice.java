package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class BountifulSacrifice extends Boon {
    @Override
    public String name() {
        return "Bountiful Sacrifice";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_BountifulSacrifice;
    }

    @Override
    public String description() {
        return "Provides a 300% XP Boost when using the Bonecrusher or Bonecrusher Necklace";
    }

    @Override
    public int item() {
        return 13116;
    }
}
