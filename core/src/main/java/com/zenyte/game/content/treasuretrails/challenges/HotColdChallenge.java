package com.zenyte.game.content.treasuretrails.challenges;

import com.zenyte.game.world.entity.Location;

public class HotColdChallenge implements ClueChallenge {
    private final Location center;

    public HotColdChallenge(Location center) {
        this.center = center;
    }

    public Location getCenter() {
        return center;
    }

    @Override
    public String toString() {
        return "HotColdChallenge(center=" + this.getCenter() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof HotColdChallenge)) return false;
        final HotColdChallenge other = (HotColdChallenge) o;
        if (!other.canEqual(this)) return false;
        final Object this$center = this.getCenter();
        final Object other$center = other.getCenter();
        return this$center == null ? other$center == null : this$center.equals(other$center);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof HotColdChallenge;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $center = this.getCenter();
        result = result * PRIME + ($center == null ? 43 : $center.hashCode());
        return result;
    }
}
