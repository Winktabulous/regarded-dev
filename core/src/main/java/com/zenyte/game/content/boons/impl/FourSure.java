package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class FourSure extends Boon {

    @Override
    public String name() {
        return "Four? Sure!";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_FourSure;
    }

    @Override
    public String description() {
        return "Using a Scythe of Vitur no longer consumes charges and guarantees a fourth hit";
    }

    @Override
    public int item() {
        return 22486;
    }

    @Override
    public String getAlternateName() {
        return "VitursOffering";
    }
}
