package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class SuperiorSorcery extends Boon {
    public static boolean roll() {
        return Utils.random(3) >= 1;
    }

    @Override
    public String name() {
        return "Superior Sorcery";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SuperiorSorcery;
    }

    @Override
    public String description() {
        return "Provides a 75% chance to save the runes they use when casting spells, including charges from powered staves.";
    }

    @Override
    public int item() {
        return 565;
    }
}
