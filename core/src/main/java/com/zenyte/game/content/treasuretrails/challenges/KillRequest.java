package com.zenyte.game.content.treasuretrails.challenges;

import java.util.Arrays;

/**
 * @author Kris | 07/04/2019 13:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class KillRequest implements ClueChallenge, ClueWithNpcs {
    private final int[] validNPCs;

    public KillRequest(int[] validNPCs) {
        this.validNPCs = validNPCs;
    }

    @Override
    public int[] getValidNPCs() {
        return validNPCs;
    }

    @Override
    public String toString() {
        return "KillRequest(validNPCs=" + Arrays.toString(this.getValidNPCs()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof KillRequest)) return false;
        final KillRequest other = (KillRequest) o;
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
