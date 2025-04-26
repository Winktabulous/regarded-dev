package com.zenyte.game.world.entity.player.dailychallenge;

import com.zenyte.game.world.entity.player.dailychallenge.challenge.DailyChallenge;

/**
 * @author Tommeh | 02/05/2019 | 22:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ChallengeProgress {

    private final DailyChallenge challenge;
    private int progress;
    private boolean claimed;

    public ChallengeProgress(final DailyChallenge challenge) {
        this.challenge = challenge;
    }

    public void progress(final int progress) {
        if (this.progress + progress > challenge.getLength()) {
            this.progress = challenge.getLength();
        } else {
            this.progress += progress;
        }
    }

    private void progress() {
        progress(1);
    }

    public boolean isCompleted() {
        return this.progress == challenge.getLength();
    }

    @Override
    public String toString() {
        return challenge.getName() + " (" + progress + "/" + challenge.getLength() + ")";
    }

    public DailyChallenge getChallenge() {
        return challenge;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }
}
