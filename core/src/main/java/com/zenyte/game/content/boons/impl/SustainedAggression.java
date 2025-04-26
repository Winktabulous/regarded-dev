package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class SustainedAggression extends Boon {
    @Override
    public String name() {
        return "Sustained Aggression";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SustainedAggression;
    }

    @Override
    public String description() {
        return "Monsters remain aggressive for 50% longer in any area";
    }

    @Override
    public int item() {
        return ItemId.HELLHOUND;
    }
}
