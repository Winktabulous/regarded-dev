package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class LessIsMore extends Boon {
    @Override
    public String name() {
        return "Less is More";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_LessIsMore;
    }

    @Override
    public String description() {
        return "Provides a passive boost that reduces clue scroll steps by 1.";
    }

    @Override
    public int item() {
        return 2575;
    }
}
