package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class TrackStar extends Boon {
    @Override
    public String name() {
        return "Track Star";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_TrackStar;
    }

    @Override
    public String description() {
        return "Provides passive Agility xp gains whilst running.";
    }

    @Override
    public int item() {
        return 11849;
    }
}
