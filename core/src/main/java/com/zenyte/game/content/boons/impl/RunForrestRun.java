package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class RunForrestRun extends Boon {

    @Override
    public String name() {
        return "Run Forrest, Run!";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_RunForrestRun;
    }

    @Override
    public String description() {
        return "Provides infinite run energy. (excluding Wilderness Areas)";
    }

    @Override
    public int item() {
        return 11860;
    }
}
