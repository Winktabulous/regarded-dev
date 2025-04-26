package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class HoleyMoley extends Boon {
    @Override
    public String name() {
        return "Holey Moley";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HoleyMoley;
    }

    @Override
    public String description() {
        return "Mole skins and mole claws are now doubled upon dropping from the Giant Mole";
    }

    @Override
    public int item() {
        return ItemId.MOLE_CLAW;
    }
}
