package com.zenyte.game.content.pyramidplunder;

/**
 * @author Christopher
 * @since 4/4/2020
 */
public class WeightedPlunderRewardTier {
    private final PlunderRewardTier tier;
    private final int weight;

    public WeightedPlunderRewardTier(PlunderRewardTier tier, int weight) {
        this.tier = tier;
        this.weight = weight;
    }

    public PlunderRewardTier getTier() {
        return tier;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "WeightedPlunderRewardTier(tier=" + this.getTier() + ", weight=" + this.getWeight() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof WeightedPlunderRewardTier)) return false;
        final WeightedPlunderRewardTier other = (WeightedPlunderRewardTier) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getWeight() != other.getWeight()) return false;
        final Object this$tier = this.getTier();
        final Object other$tier = other.getTier();
        if (this$tier == null ? other$tier != null : !this$tier.equals(other$tier)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof WeightedPlunderRewardTier;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getWeight();
        final Object $tier = this.getTier();
        result = result * PRIME + ($tier == null ? 43 : $tier.hashCode());
        return result;
    }
}
