package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class NoShardRequired extends Boon {
    @Override
    public String name() {
        return "No Shard Required I";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_NoShardRequired;
    }

    @Override
    public String description() {
        return "Grants the blood fury effect for melee damage without requiring the equipment. (10% proc rate)";
    }

    @Override
    public int item() {
        return 24777;
    }
}
