package com.zenyte.game.model.polls;

import com.google.gson.annotations.Expose;

/**
 * @author Kris | 26. march 2018 : 3:35.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PollAnswer {

    /**
     * The name of the choice in the poll.
     */
    @Expose
    private String choice;

    /**
     * The number of votes this choice has received.
     */
    @Expose
    private int votes;

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

}
