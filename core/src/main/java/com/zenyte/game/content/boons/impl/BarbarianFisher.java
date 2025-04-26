package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.util.Utils;

import static com.zenyte.game.content.boons.BoonPriceTable.v_BarbarianFisher;

public class BarbarianFisher extends Boon {
    public static boolean roll() {
        return Utils.random(1) == 1;
    }

    @Override
    public String name() {
        return "Barbarian Fisher";
    }

    @Override
    public int price() {
        return v_BarbarianFisher;
    }

    @Override
    public String description() {
        return "Provides a 50% chance to receive equal strength and agility experience upon catching a fish";
    }

    @Override
    public int item() {
        return 11323;
    }
}
