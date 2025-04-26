package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.content.boons.DisabledBoon;

@DisabledBoon
public class Enlightened extends Boon {
    @Override
    public String name() {
        return "Enlightened";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_Enlightened;
    }

    @Override
    public String description() {
        return "Provides double xp in all non-combat skills.";
    }

    @Override
    public int item() {
        return 12610;
    }

    @Override
    public boolean deactivated() {
        return true;
    }
}
