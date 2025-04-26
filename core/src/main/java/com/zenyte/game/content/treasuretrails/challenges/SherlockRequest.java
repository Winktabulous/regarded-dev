package com.zenyte.game.content.treasuretrails.challenges;

import com.zenyte.game.content.treasuretrails.clues.SherlockTask;

public class SherlockRequest implements ClueChallenge {
    private final SherlockTask task;

    public SherlockRequest(SherlockTask task) {
        this.task = task;
    }

    public SherlockTask getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "SherlockRequest(task=" + this.getTask() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SherlockRequest)) return false;
        final SherlockRequest other = (SherlockRequest) o;
        if (!other.canEqual(this)) return false;
        final Object this$task = this.getTask();
        final Object other$task = other.getTask();
        return this$task == null ? other$task == null : this$task.equals(other$task);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SherlockRequest;
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
