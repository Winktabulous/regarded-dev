package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class EyeDontSeeYou extends Boon {
    @Override
    public String name() {
        return "Eye Don't See You";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_EyeDontSeeYou;
    }

    @Override
    public String description() {
        return "Cuts the damage from Duke's special attack by 66%";
    }

    @Override
    public int item() {
        return ItemId.EYE_OF_THE_DUKE;
    }
}
