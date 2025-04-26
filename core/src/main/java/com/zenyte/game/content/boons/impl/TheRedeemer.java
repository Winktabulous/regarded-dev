package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class TheRedeemer extends Boon {
    @Override
    public String name() {
        return "The Redeemer";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_TheRedeemer;
    }

    @Override
    public String description() {
        return "The Redemption prayer now heals you to 50 hp or your max hp (whichever is lower)";
    }

    @Override
    public int item() {
        return ItemId.PRAYER_BOOK;
    }
}
