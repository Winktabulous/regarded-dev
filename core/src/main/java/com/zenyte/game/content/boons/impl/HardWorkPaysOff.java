package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class HardWorkPaysOff extends Boon {

    /* 10% */
    public static boolean roll() {
        return Utils.random(9) == 1;
    }
    @Override
    public String name() {
        return "Hard Work Pays Off";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HardWorkPaysOff;
    }

    @Override
    public String description() {
        return "Grants a 10% chance to instantly complete a clue scroll after finishing a step.";
    }

    @Override
    public int item() {
        return 19835;
    }
}
