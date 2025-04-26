package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class Pyromaniac extends Boon {
    public static final String NO_PERK_MESSAGE = "You must have Pyromaniac unlocked to be able to do that.";

    public static boolean rollPage() {
        return Utils.random(24) == 0;
    }

    @Override
    public String name() {
        return "Pyromaniac";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_Pyromaniac;
    }

    @Override
    public String description() {
        return "Provides an ability to use logs on any open flames as a bonfire and can award burnt pages.";
    }

    @Override
    public int item() {
        return 590;
    }
}
