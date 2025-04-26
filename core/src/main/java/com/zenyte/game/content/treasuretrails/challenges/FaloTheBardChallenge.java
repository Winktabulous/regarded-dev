package com.zenyte.game.content.treasuretrails.challenges;

import com.zenyte.game.content.treasuretrails.clues.FaloTheBardClue;

public class FaloTheBardChallenge implements ClueChallenge {
    private final FaloTheBardClue task;

    public FaloTheBardChallenge(FaloTheBardClue task) {
        this.task = task;
    }

    public FaloTheBardClue getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "FaloTheBardChallenge(task=" + this.getTask() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof FaloTheBardChallenge)) return false;
        final FaloTheBardChallenge other = (FaloTheBardChallenge) o;
        if (!other.canEqual(this)) return false;
        final Object this$task = this.getTask();
        final Object other$task = other.getTask();
        return this$task == null ? other$task == null : this$task.equals(other$task);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof FaloTheBardChallenge;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $task = this.getTask();
        result = result * PRIME + ($task == null ? 43 : $task.hashCode());
        return result;
    }
}
