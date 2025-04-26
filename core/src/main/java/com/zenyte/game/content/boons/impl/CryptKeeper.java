package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class CryptKeeper extends Boon {
    @Override
    public String name() {
        return "Crypt Keeper";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_CryptKeeper;
    }

    @Override
    public String description() {
        return "Provides two additional loot rolls at Barrows.";
    }

    @Override
    public int item() {
        return 952;
    }
}
