package com.zenyte.game.content.skills.farming.contract;

import com.zenyte.game.world.entity.player.Player;

import java.util.function.Predicate;

/**
 * @author Christopher
 * @since 4/10/2020
 */
public class FarmingContractRoll {
    public static final FarmingContractRoll NONE = new FarmingContractRoll(0, 0, player -> false);
    private final int lowInclusive;
    private final int highInclusive;
    private final Predicate<Player> predicate;

    FarmingContractRoll(final int lowInclusive, final int highInclusive) {
        this.lowInclusive = lowInclusive;
        this.highInclusive = highInclusive;
        this.predicate = player -> true;
    }

    public FarmingContractRoll(int lowInclusive, int highInclusive, Predicate<Player> predicate) {
        this.lowInclusive = lowInclusive;
        this.highInclusive = highInclusive;
        this.predicate = predicate;
    }

    public int getLowInclusive() {
        return lowInclusive;
    }

    public int getHighInclusive() {
        return highInclusive;
    }

    public Predicate<Player> getPredicate() {
        return predicate;
    }
}
