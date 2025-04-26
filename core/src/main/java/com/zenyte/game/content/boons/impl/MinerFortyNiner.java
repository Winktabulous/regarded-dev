package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class MinerFortyNiner extends Boon {
    @Override
    public String name() {
        return "Miner Forty-Niner";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_MinerFortyNiner;
    }

    @Override
    public String description() {
        return "Provides a passive boost that prevents ores from depleting.";
    }

    @Override
    public int item() {
        return 11920;
    }
}
