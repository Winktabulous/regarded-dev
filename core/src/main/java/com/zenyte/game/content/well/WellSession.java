package com.zenyte.game.content.well;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class WellSession {

    private WellPerk perk;
    private long coins;
    private ObjectArrayList<WellContribution> contributions;
    private int cycle;

    public WellPerk getPerk() {
        return perk;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public boolean isActived() {
        return cycle > 0;
    }

    public ObjectArrayList<WellContribution> getContributions() {
        return contributions;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int decrementCycle() {
        return cycle--;
    }

    public WellSession(WellPerk perk, int coins, ObjectArrayList<WellContribution> contributions, int cycle) {
        this.perk = perk;
        this.coins = coins;
        this.contributions = contributions;
        this.cycle = cycle;
    }

    public void incrementCoins(int amt) {
        coins+=amt;
    }
}
