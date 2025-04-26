package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class NoShardRequiredIII extends Boon {
    @Override
    public String name() {
        return "No Shard Required III";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_NoShardRequiredIII;
    }

    @Override
    public String description() {
        return "Grants the blood fury effect for magic damage without requiring the equipment. (10% proc rate)";
    }

    @Override
    public int item() {
        return 24777;
    }
}
