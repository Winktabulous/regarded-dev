package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class EndlessQuiver extends Boon {
    @Override
    public String name() {
        return "Endless Quiver";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_EndlessQuiver;
    }

    @Override
    public String description() {
        return "Provides the passive effect of Ava's Attractor without having it equipped.";
    }

    @Override
    public int item() {
        return ItemId.AVAS_ACCUMULATOR;
    }
}
