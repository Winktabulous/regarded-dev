package com.zenyte.game.world.entity.player.dailychallenge;

import java.util.Arrays;

public class ChallengeDetails {
    private final ChallengeDifficulty difficulty;
    private final Object[] additionalInformation;

    public ChallengeDetails(final ChallengeDifficulty difficulty, final Object... additionalInformation) {
        this.difficulty = difficulty;
        this.additionalInformation = additionalInformation;
    }

    public ChallengeDifficulty getDifficulty() {
        return difficulty;
    }

    public Object[] getAdditionalInformation() {
        return additionalInformation;
    }

    @Override
    public String toString() {
        return "ChallengeDetails(difficulty=" + this.getDifficulty() + ", additionalInformation=" + Arrays.deepToString(this.getAdditionalInformation()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ChallengeDetails)) return false;
        final ChallengeDetails other = (ChallengeDetails) o;
        if (!other.canEqual(this)) return false;
        final Object this$difficulty = this.getDifficulty();
        final Object other$difficulty = other.getDifficulty();
        if (this$difficulty == null ? other$difficulty != null : !this$difficulty.equals(other$difficulty)) return false;
        return Arrays.deepEquals(this.getAdditionalInformation(), other.getAdditionalInformation());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ChallengeDetails;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $difficulty = this.getDifficulty();
        result = result * PRIME + ($difficulty == null ? 43 : $difficulty.hashCode());
        result = result * PRIME + Arrays.deepHashCode(this.getAdditionalInformation());
        return result;
    }
}
