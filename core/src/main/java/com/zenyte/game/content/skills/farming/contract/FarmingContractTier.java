package com.zenyte.game.content.skills.farming.contract;

import com.google.common.base.Preconditions;
import com.zenyte.game.util.Utils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 4/7/2020
 */
public enum FarmingContractTier {
    ONE(1, 6, new FarmingContractRoll(3, 5), new FarmingContractRoll(1, 3), FarmingContractRoll.NONE), TWO(2, 7, new FarmingContractRoll(3, 5), new FarmingContractRoll(2, 3), new FarmingContractRoll(1, 1, player -> Utils.random(10) == 0)), THREE(3, 8, new FarmingContractRoll(3, 6), new FarmingContractRoll(2, 4), new FarmingContractRoll(0, 1)), FOUR(4, 9, new FarmingContractRoll(2, 5), new FarmingContractRoll(3, 5), new FarmingContractRoll(1, 2)), FIVE(5, 10, new FarmingContractRoll(1, 5), new FarmingContractRoll(4, 6), new FarmingContractRoll(1, 3));
    private static final Int2ObjectMap<FarmingContractTier> tiersById = new Int2ObjectOpenHashMap<>();
    private static final ObjectList<FarmingContractTier> tiers = ObjectArrayList.wrap(values());
    private static final long TIER_ONE_MINIMUM_MINS = 0;
    private static final long TIER_TWO_MINIMUM_MINS = 80;
    private static final long TIER_THREE_MINIMUM_MINS = 200;

    static {
        for (FarmingContractTier tier : tiers) {
            tiersById.put(tier.getTierId(), tier);
        }
    }

    private final int tierId;
    private final int maxRolls;
    private final FarmingContractRoll lowRolls;
    private final FarmingContractRoll mediumRolls;
    private final FarmingContractRoll highRolls;

    public static FarmingContractTier get(@NonNegative final int growthMinutes, @NotNull final FarmingContractDifficulty difficulty) {
        final FarmingContractTier baseTier = getTierByLength(growthMinutes);
        return baseTier.increase(difficulty.getTierBonus());
    }

    public static FarmingContractTier getById(@NonNegative final int tierId) {
        final FarmingContractTier tier = tiersById.get(tierId);
        Preconditions.checkNotNull(tier, "Could not find tier with id: " + tierId);
        return tier;
    }

    private static FarmingContractTier getTierByLength(@NonNegative final int growthTimeMinutes) {
        if (growthTimeMinutes >= TIER_THREE_MINIMUM_MINS) {
            return THREE;
        } else if (growthTimeMinutes >= TIER_TWO_MINIMUM_MINS) {
            return TWO;
        } else {
            return ONE;
        }
    }

    private FarmingContractTier increase(@Positive int amount) {
        final int newIndex = Math.min(tiers.size() - 1, tiers.indexOf(this) + amount);
        return tiers.get(newIndex);
    }

    FarmingContractTier(int tierId, int maxRolls, FarmingContractRoll lowRolls, FarmingContractRoll mediumRolls, FarmingContractRoll highRolls) {
        this.tierId = tierId;
        this.maxRolls = maxRolls;
        this.lowRolls = lowRolls;
        this.mediumRolls = mediumRolls;
        this.highRolls = highRolls;
    }

    public int getTierId() {
        return tierId;
    }

    public int getMaxRolls() {
        return maxRolls;
    }

    public FarmingContractRoll getLowRolls() {
        return lowRolls;
    }

    public FarmingContractRoll getMediumRolls() {
        return mediumRolls;
    }

    public FarmingContractRoll getHighRolls() {
        return highRolls;
    }
}
