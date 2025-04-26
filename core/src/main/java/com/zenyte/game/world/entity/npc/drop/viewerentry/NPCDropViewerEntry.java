package com.zenyte.game.world.entity.npc.drop.viewerentry;

/**
 * @author Tommeh | 05/10/2019 | 19:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class NPCDropViewerEntry implements DropViewerEntry {

    private final int itemId;
    private final int npc;
    private final int minAmount, maxAmount;
    private final double rate;
    private final String info;

    @Override
    public int minAmount() {
        return minAmount;
    }

    @Override
    public int maxAmount() {
        return maxAmount;
    }

    @Override
    public double rate() {
        return rate;
    }

    @Override
    public String info() {
        return info;
    }

    @Override
    public boolean isPredicated() {
        return !info.isEmpty();
    }

    public NPCDropViewerEntry(int itemId, int npc, int minAmount, int maxAmount, double rate, String info) {
        this.itemId = itemId;
        this.npc = npc;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rate = rate;
        this.info = info;
    }

    public int getItemId() {
        return itemId;
    }

    public int getNpc() {
        return npc;
    }
}
