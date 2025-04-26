package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

public class HolyInterventionIII extends Boon {
    @Override
    public String name() {
        return "Holy Intervention III";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_HolyInterventionIII;
    }

    @Override
    public String description() {
        return "Reduces prayer drain speed by another 25% (50% total)";
    }

    @Override
    public int item() {
        return ItemId.PRAYER_POTION4;
    }

    @Override
    public boolean purchasable(Player p) {
        return p.hasBoon(HolyInterventionII.class);
    }

}
