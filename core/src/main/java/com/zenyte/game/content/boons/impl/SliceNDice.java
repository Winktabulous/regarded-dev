package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class SliceNDice extends Boon {
    public static boolean roll() {
        return Utils.random(3) == 1;
    }
    @Override
    public String name() {
        return "Slice n' Dice";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SliceNDice;
    }

    @Override
    public String description() {
        return "Provides a 25% chance to strike an additional hit on a slash special attack.";
    }

    @Override
    public int item() {
        return 1215;
    }
}
