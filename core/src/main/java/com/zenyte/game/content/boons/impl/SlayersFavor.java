package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class SlayersFavor extends Boon {
    @Override
    public String name() {
        return "Slayer's Favor";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SlayersFavor;
    }

    @Override
    public String description() {
        return "Provides a passive boost that raises superior slayer monster spawn rate by 25%";
    }

    @Override
    public int item() {
        return ItemId.MYSTIC_AIR_STAFF;
    }
}
