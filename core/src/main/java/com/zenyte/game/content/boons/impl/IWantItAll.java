package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class IWantItAll extends Boon {

    public static final double GATHER_QUANTITY_MULTIPLIER = 2.0;

    public static boolean roll() {
        return Utils.random(1) == 0;
    }

    @Override
    public String name() {
        return "I Want It All";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_IWantItAll;
    }

    @Override
    public String description() {
        return "Provides a 50% chance to double skilling resources from a gathering skill.";
    }

    @Override
    public int item() {
        return 21347;
    }
}
