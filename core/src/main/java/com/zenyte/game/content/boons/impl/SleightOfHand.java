package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class SleightOfHand extends Boon {
    @Override
    public String name() {
        return "Sleight of Hand";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SleightOfHand;
    }

    @Override
    public String description() {
        return "Provides the ability to automatically re-pickpocket an npc until you can no longer do so.";
    }

    @Override
    public int item() {
        return 22521;
    }
}
