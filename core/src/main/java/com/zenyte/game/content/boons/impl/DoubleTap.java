package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class DoubleTap extends Boon {
    public static boolean roll() {
        return Utils.random(4) == 0;
    }

    @Override
    public String name() {
        return "Double Tap";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_DoubleTap;
    }

    @Override
    public String description() {
        return "Provides a 20% chance for your cannon to shoot two cannonballs at once.";
    }

    @Override
    public int item() {
        return 21728;
    }
}
