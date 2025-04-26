package com.zenyte.game.content.treasuretrails.challenges;

import com.zenyte.game.content.treasuretrails.clues.ChallengeScroll;

import java.util.Arrays;

public final class TalkChallengeRequest implements ClueChallenge, ClueWithNpcs {
    private final ChallengeScroll challengeScroll;
    private final int[] validNPCs;

    public TalkChallengeRequest(ChallengeScroll challengeScroll, int[] validNPCs) {
        this.challengeScroll = challengeScroll;
        this.validNPCs = validNPCs;
    }

    public ChallengeScroll getChallengeScroll() {
        return challengeScroll;
    }

    @Override
    public int[] getValidNPCs() {
        return validNPCs;
    }

    @Override
    public String toString() {
        return "TalkChallengeRequest(challengeScroll=" + this.getChallengeScroll() + ", validNPCs=" + Arrays.toString(this.getValidNPCs()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TalkChallengeRequest)) return false;
        final TalkChallengeRequest other = (TalkChallengeRequest) o;
        final Object this$challengeScroll = this.getChallengeScroll();
        final Object other$challengeScroll = other.getChallengeScroll();
        if (this$challengeScroll == null ? other$challengeScroll != null : !this$challengeScroll.equals(other$challengeScroll)) return false;
        return Arrays.equals(this.getValidNPCs(), other.getValidNPCs());
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $challengeScroll = this.getChallengeScroll();
        result = result * PRIME + ($challengeScroll == null ? 43 : $challengeScroll.hashCode());
        result = result * PRIME + Arrays.hashCode(this.getValidNPCs());
        return result;
    }
}
