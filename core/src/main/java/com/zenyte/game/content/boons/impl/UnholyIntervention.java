package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class UnholyIntervention extends Boon {
    public static final int MAX_HEAL = 20;
    public static final int MIN_HEAL = 10;

    @Override
    public String name() {
        return "Unholy Intervention";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_UnholyIntervention;
    }

    @Override
    public String description() {
        return "The special attacks of Demonic Gorillas will now heal you instead of damage you.";
    }

    @Override
    public int item() {
        return 19529;
    }


}
