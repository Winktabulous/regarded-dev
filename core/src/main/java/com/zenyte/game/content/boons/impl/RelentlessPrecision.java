package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.util.Utils;

public class RelentlessPrecision extends Boon {

    public static Special determineBoost() {
        int random = Utils.random(24);
        if(random == 24)
            return Special.BOTH;
        if(random > 19)
            return Special.BYPASS_DEF;
        if(random > 14)
            return Special.EXTRA_HIT;
        return Special.NONE;
    }

    public enum Special {
        EXTRA_HIT, BYPASS_DEF, BOTH, NONE
    }

    @Override
    public String name() {
        return "Relentless Precision";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_RelentlessPrecision;
    }

    @Override
    public String description() {
        return "With a Twisted Bow, provides a 1/5 chance to double hit, a 1/5 chance to bypass defense, and 1/25 chance to do both effects.";
    }

    @Override
    public String getAlternateName() {
        return "TwistedTradeOff";
    }

    @Override
    public int item() {
        return 20997;
    }
}
