package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;

public class FarmersFortune extends Boon {
    public static boolean roll() {
        return Utils.random(9) <= 1;
    }

    @Override
    public String name() {
        return "Farmer's Fortune";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_FarmersFortune;
    }

    @Override
    public String description() {
        return "Provides a 20% chance to preserve a seed or sapling when being planted into a patch.";
    }

    @Override
    public int item() {
        return ItemId.YEW_SAPLING;
    }
}
