package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

public class HolyInterventionII extends Boon {
    @Override
    public String name() {
        return "Holy Intervention II";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HolyInterventionII;
    }

    @Override
    public String description() {
        return "Reduces prayer drain speed by another 15% (25% total)";
    }

    @Override
    public int item() {
        return ItemId.PRAYER_POTION2;
    }

    @Override
    public boolean purchasable(Player p) {
        return p.hasBoon(HolyInterventionI.class);
    }


}
