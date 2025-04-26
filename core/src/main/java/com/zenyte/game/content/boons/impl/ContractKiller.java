package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class ContractKiller extends Boon {
    @Override
    public String name() {
        return "Contract Killer";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_ContractKiller;
    }

    @Override
    public String description() {
        return "Expeditious and Slaughter bracelets have a 35% proc rate and no longer degrade.";
    }

    @Override
    public int item() {
        return ItemId.SLAYER_RING_ETERNAL;
    }
}
