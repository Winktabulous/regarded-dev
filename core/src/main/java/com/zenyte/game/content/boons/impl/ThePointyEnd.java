package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class ThePointyEnd extends Boon {
    @Override
    public String name() {
        return "The Pointy End";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_ThePointyEnd;
    }

    @Override
    public String description() {
        return "Using a stab weapon ignores 10% of the enemy target's defense (PVM).";
    }

    @Override
    public int item() {
        return ItemId.ZAMORAKIAN_SPEAR;
    }
}
