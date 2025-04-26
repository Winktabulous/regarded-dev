package com.zenyte.game.content.treasuretrails.challenges;

import com.zenyte.game.world.entity.player.Player;

import java.util.Arrays;
import java.util.function.Predicate;

public final class TalkRequest implements ClueChallenge, ClueWithNpcs {
    private final int[] validNPCs;
    private Predicate<Player> predicate;

    public TalkRequest(int[] validNPCs, Predicate<Player> predicate) {
        this.validNPCs = validNPCs;
        this.predicate = predicate;
    }

    public TalkRequest(int[] validNPCs) {
        this.validNPCs = validNPCs;
    }

    @Override
    public int[] getValidNPCs() {
        return validNPCs;
    }

    public Predicate<Player> getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate<Player> predicate) {
        this.predicate = predicate;
    }

    @Override
    public String toString() {
        return "TalkRequest(validNPCs=" + Arrays.toString(this.getValidNPCs()) + ", predicate=" + this.getPredicate() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TalkRequest)) return false;
        final TalkRequest other = (TalkRequest) o;
        if (!Arrays.equals(this.getValidNPCs(), other.getValidNPCs())) return false;
        final Object this$predicate = this.getPredicate();
        final Object other$predicate = other.getPredicate();
        return this$predicate == null ? other$predicate == null : this$predicate.equals(other$predicate);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + Arrays.hashCode(this.getValidNPCs());
        final Object $predicate = this.getPredicate();
        result = result * PRIME + ($predicate == null ? 43 : $predicate.hashCode());
        return result;
    }
}
