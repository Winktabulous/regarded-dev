package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

public class HolyInterventionI extends Boon {
    @Override
    public String name() {
        return "Holy Intervention I";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HolyInterventionI;
    }

    @Override
    public String description() {
        return "Reduces prayer drain speed by 10%";
    }

    @Override
    public int item() {
        return ItemId.PRAYER_POTION1;
    }
}
