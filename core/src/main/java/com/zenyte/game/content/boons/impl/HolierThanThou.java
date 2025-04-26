package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class HolierThanThou extends Boon {
    @Override
    public String name() {
        return "Holier Than Thou";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HolierThanThou;
    }

    @Override
    public String description() {
        return "Provides a passive 15% damage boost to the Angel of Death";
    }

    @Override
    public int item() {
        return ItemId.ANGELIC_ARTIFACT;
    }
}
