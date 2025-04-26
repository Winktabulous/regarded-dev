package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class DoubleChins extends Boon {
    @Override
    public String name() {
        return "Double Chins";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_DoubleChins;
    }

    @Override
    public String description() {
        return "Doubles loot, catch rate (invisible 20 level boost), and Hunter XP when hunting chinchompas.";
    }

    @Override
    public int item() {
        return ItemId.BLACK_CHINCHOMPA;
    }
}
