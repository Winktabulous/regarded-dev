package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class HoarderMentality extends Boon {
    @Override
    public String name() {
        return "Hoarder Mentality";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HoarderMentality;
    }

    @Override
    public String description() {
        return "Causes dragons to now drop bones and hides as their noted form.";
    }

    @Override
    public int item() {
        return 1754;
    }
}
