package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class DagaWHO extends Boon {
    @Override
    public String name() {
        return "Daga-WHO?";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_DagaWHO;
    }

    @Override
    public String description() {
        return "Provides an ability to damage Dagannoth Kings with any combat style.";
    }

    @Override
    public int item() {
        return 7857;
    }
}
