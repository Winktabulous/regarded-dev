package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class Alchoholic extends Boon {
    @Override
    public String name() {
        return "Alchoholic";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_Alchoholic;
    }

    @Override
    public String description() {
        return "Increases alchemy casting speed by 2 ticks.";
    }

    @Override
    public int item() {
        return ItemId.WIZARDS_MIND_BOMB;
    }
}
