package com.zenyte.game.content.skills.slayer;

import com.zenyte.game.world.region.RegionArea;

/**
 * @author Kris | 5. nov 2017 : 21:22.52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class Task {

    private final SlayerMaster slayerMaster;
    private final int weight, minimumAmount, maximumAmount;
    private final Class<? extends RegionArea>[] areas;

    public Task(final SlayerMaster master, final int weight, final int minimumAmount, final int maximumAmount) {
        this(master, weight, minimumAmount, maximumAmount, (Class<? extends RegionArea>[]) null);
    }

    @SafeVarargs
    public Task(final SlayerMaster master, final int weight, final int minimumAmount, final int maximumAmount, final Class<? extends RegionArea>... areas) {
        this.slayerMaster = master;
        this.weight = weight;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.areas = areas;
    }

    public SlayerMaster getSlayerMaster() {
        return slayerMaster;
    }

    public int getWeight() {
        return weight;
    }

    public int getMinimumAmount() {
        return minimumAmount;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }

    public Class<? extends RegionArea>[] getAreas() {
        return areas;
    }
}
