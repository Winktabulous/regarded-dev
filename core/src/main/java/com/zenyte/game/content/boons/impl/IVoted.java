package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class IVoted extends Boon {

    public static boolean roll() {
        return Utils.random(9) <= 2;
    }

    @Override
    public String name() {
        return "I Voted";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_IVoted;
    }

    @Override
    public String description() {
        return "Provides a 30% chance to double vote rewards when claiming";
    }

    @Override
    public int item() {
        return 2996;
    }
}
