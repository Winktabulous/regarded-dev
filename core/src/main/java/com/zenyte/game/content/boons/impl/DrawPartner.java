package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class DrawPartner extends Boon {
    @Override
    public String name() {
        return "Draw, Partner";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_DrawPartner;
    }

    @Override
    public String description() {
        return "Provides a 1 tick increase for all crossbow weapons.";
    }

    @Override
    public int item() {
        return 21902;
    }
}
