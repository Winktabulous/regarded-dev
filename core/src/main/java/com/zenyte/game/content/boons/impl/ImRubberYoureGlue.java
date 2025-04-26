package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class ImRubberYoureGlue extends Boon {
    @Override
    public String name() {
        return "I'm Rubber, You're Glue";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_ImRubberYoureGlue;
    }

    @Override
    public String description() {
        return "Provides a permanent ring of recoil effect (does not stack with actual ring).";
    }

    @Override
    public int item() {
        return 2550;
    }
}
