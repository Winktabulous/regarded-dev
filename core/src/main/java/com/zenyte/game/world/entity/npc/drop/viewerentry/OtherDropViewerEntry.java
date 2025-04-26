package com.zenyte.game.world.entity.npc.drop.viewerentry;

public record OtherDropViewerEntry(int itemId, int minAmount, int maxAmount, double rate, double totalWeight,
                                   String info) implements DropViewerEntry {

    @Override
    public boolean isPredicated() {
        return !info.isEmpty();
    }

    public String toPercentage() {
        return formatDropRate((rate / totalWeight) * 100) + "%";
    }

    public static String formatDropRate(double rate) {
        return String.format(rate < 0.001 ? "%.4f" :
                rate < 0.01 ? "%.3f" : "%.2f", rate);
    }

    public String toFractional() {
        return "1 / " + Math.round(( 1 / ( (rate / totalWeight))));
    }

    public double rate() {
        return rate / totalWeight;
    }
}
