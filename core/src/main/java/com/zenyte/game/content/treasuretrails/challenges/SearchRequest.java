package com.zenyte.game.content.treasuretrails.challenges;

import java.util.Arrays;

public final class SearchRequest implements ClueChallenge, ClueWithObjects {
    private final GameObject[] validObjects;

    public SearchRequest(GameObject[] validObjects) {
        this.validObjects = validObjects;
    }

    @Override
    public GameObject[] getValidObjects() {
        return validObjects;
    }

    @Override
    public String toString() {
        return "SearchRequest(validObjects=" + Arrays.deepToString(this.getValidObjects()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SearchRequest)) return false;
        final SearchRequest other = (SearchRequest) o;
        return Arrays.deepEquals(this.getValidObjects(), other.getValidObjects());
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + Arrays.deepHashCode(this.getValidObjects());
        return result;
    }
}
