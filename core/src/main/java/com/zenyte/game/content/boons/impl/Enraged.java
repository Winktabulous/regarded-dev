package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.content.boons.DisabledBoon;

@DisabledBoon
public class Enraged extends Boon {
    @Override
    public String name() {
        return "Enraged";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_Enraged;
    }

    @Override
    public String description() {
        return "Provides double xp in all combat skills.";
    }

    @Override
    public int item() {
        return 12608;
    }

    @Override
    public boolean deactivated() {
        return true;
    }
}
