package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class DivineHealing extends Boon {
    public static DivineHealing.Special determineBoost() {
        int random = Utils.random(24);
        if(random == 24)
            return DivineHealing.Special.BOTH;
        if(random > 19)
            return DivineHealing.Special.HEAL_HP;
        if(random > 14)
            return DivineHealing.Special.HEAL_PRAY;
        return DivineHealing.Special.NONE;
    }

    public enum Special {
        HEAL_PRAY, HEAL_HP, BOTH, NONE
    }

    @Override
    public String name() {
        return "Divine Healing";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_DivineHealing;
    }

    @Override
    public String description() {
        return "When using Tumeken's Shadow, provides 1/5 chance to heal hp, 1/5 chance to heal prayer, and 1/25 chance to do both effects";
    }

    @Override
    public int item() {
        return 27277;
    }

    @Override
    public String getAlternateName() {
        return "TumekensTribute";
    }
}
