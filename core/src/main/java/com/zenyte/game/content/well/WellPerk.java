package com.zenyte.game.content.well;

import com.zenyte.game.content.serverevent.WorldBoostType;
import com.zenyte.game.world.broadcasts.BroadcastType;

public enum WellPerk implements WorldBoostType {

    BONUS_XP("+50% Exp", 200_000_000),
    DOUBLE_VOTE_POINTS("Double Vote Points", 200_000_000),
    DOUBLE_BLOOD_MONEY("Double Blood Money", 250_000_000),
    DOUBLE_UNIQUES("Double Unique Drops", 4_000_000_000L);

    public static final WellPerk[] VALUES = values();

    private final String mssg;

    private final long amount;

    public long getAmount() {
        return amount;
    }

    WellPerk(String mssg, long amount) {
        this.mssg = mssg;
        this.amount = amount;
    }

    @Override
    public String getMssg() {
        return mssg;
    }

    @Override
    public BroadcastType getBroadcastType() {
        return BroadcastType.WELL_OF_GOODWILL;
    }
}
