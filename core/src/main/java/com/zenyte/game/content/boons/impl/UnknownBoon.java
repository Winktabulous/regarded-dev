package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;

public class UnknownBoon extends Boon {
    @Override
    public String name() {
        return "null";
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public int item() {
        return 0;
    }


}
