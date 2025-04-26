package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class BoneCruncher extends Boon {

    public static boolean roll() {
        return Utils.random(3) == 1;
    }

    @Override
    public String name() {
        return "Bone Cruncher";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_BoneCruncher;
    }

    @Override
    public String description() {
        return "Provides a 25% chance to save bones. (stacks with Wilderness Altar)";
    }

    @Override
    public int item() {
        return 11943;
    }
}
