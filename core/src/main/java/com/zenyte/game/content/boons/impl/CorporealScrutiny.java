package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class CorporealScrutiny extends Boon {
    @Override
    public String name() {
        return "Corporeal Scrutiny";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_CorporealScrutiny;
    }

    @Override
    public String description() {
        return "Provides a passive ability that allows all stab & slash sword style weapons to damage the Corporeal Beast.";
    }

    @Override
    public int item() {
        return 26219;
    }
}
