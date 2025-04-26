package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class SoulStealer extends Boon {
    @Override
    public String name() {
        return "Soul Stealer";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SoulStealer;
    }

    @Override
    public String description() {
        return "Your minimum hit is now increased by 10% of your maximum hit rounded up.";
    }

    @Override
    public int item() {
        return ItemId.SOUL_FRAGMENT_25201;
    }
}
