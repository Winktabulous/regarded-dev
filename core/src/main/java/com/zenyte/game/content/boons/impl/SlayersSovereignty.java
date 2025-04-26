package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class SlayersSovereignty extends Boon {
    @Override
    public String name() {
        return "Slayer's Sovereignty";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SlayersSovereignty;
    }

    @Override
    public String description() {
        return "Slayer helm and statue passive effects now work off task.";
    }

    @Override
    public int item() {
        return ItemId.BLACK_SLAYER_HELMET;
    }
}
