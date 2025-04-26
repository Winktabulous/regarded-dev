package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;

import static com.zenyte.game.content.boons.BoonPriceTable.v_BurnBabyBurn;

public class BurnBabyBurn extends Boon {
    @Override
    public String name() {
        return "Burn Baby Burn";
    }

    @Override
    public int price() {
        return v_BurnBabyBurn;
    }

    @Override
    public String description() {
        return "Provides resistance to dragonfire equivalent to a DFS.";
    }

    @Override
    public int item() {
        return 1540;
    }
}
