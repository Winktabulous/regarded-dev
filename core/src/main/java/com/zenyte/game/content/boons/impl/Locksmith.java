package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class Locksmith extends Boon {
    public static boolean roll() {
        // 30%
        return Utils.random(9) <= 2;
    }
    @Override
    public String name() {
        return "Locksmith";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_Locksmith;
    }

    @Override
    public String description() {
        return "Provides a 30% chance to save a crystal key when opening the crystal chest.";
    }

    @Override
    public int item() {
        return 989;
    }
}
