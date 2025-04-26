package com.zenyte.game.content.treasuretrails.challenges;

import java.util.Arrays;

public final class LightBoxRequest implements ClueChallenge, ClueWithNpcs {

    private final int[] validNPCs;

    public LightBoxRequest(int[] validNPCs) {
        this.validNPCs = validNPCs;
    }

    @Override
    public int[] getValidNPCs() {
        return validNPCs;
    }

    @Override
    public String toString() {
        return "LightBoxRequest(validNPCs=" + Arrays.toString(this.getValidNPCs()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LightBoxRequest)) return false;
        final LightBoxRequest other = (LightBoxRequest) o;
        return Arrays.equals(this.getValidNPCs(), other.getValidNPCs());
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + Arrays.hashCode(this.getValidNPCs());
        return result;
    }
}
