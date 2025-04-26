package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class TheLegendaryFisherman extends Boon {
    @Override
    public String name() {
        return "The Legendary Fisherman";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_TheLegendaryFisherman;
    }

    @Override
    public String description() {
        return "While fishing, you have the ability to catch a fish twice as often.";
    }

    @Override
    public int item() {
        return ItemId.HARPOON;
    }
}
