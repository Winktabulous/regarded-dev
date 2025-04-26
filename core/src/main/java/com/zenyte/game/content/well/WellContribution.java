package com.zenyte.game.content.well;

public class WellContribution {

    private String name;
    private long amt;

    public String getName() {
        return name;
    }

    public long getAmt() {
        return amt;
    }

    public void increment(long amt) {
        this.amt += amt;
    }

    public WellContribution(String name, long amt) {
        this.name = name;
        this.amt = amt;
    }
}
