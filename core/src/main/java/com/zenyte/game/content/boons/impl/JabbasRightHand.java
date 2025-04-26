package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class JabbasRightHand extends Boon {
    @Override
    public String name() {
        return "Jabba's Right Hand";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_JabbasRightHand;
    }

    @Override
    public String description() {
        return "Bounty Hunter weapons and armor exclusive boosts now apply in PvM as well as adding a 10% damage boost to weapons against PvM targets.";
    }

    @Override
    public int item() {
        return ItemId.ESOTERIC_EMBLEM_TIER_10;
    }
}
