package com.zenyte.game.content.chambersofxeric.score;

/**
 * @author Kris | 17. mai 2018 : 18:53:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
class Score {

    /**
     * The duration in which the raid was completed, in ticks.
     */
    private int duration;
    /**
     * The username of the owner of the clan of the raid.
     */
    private String clan;

    /**
     * The constructor for the score object that is serialized in the data folder.
     *
     * @param clan     the username of the clan owner.
     * @param duration the duration in which the raid was completed.
     */
    Score(final String clan, final int duration) {
        this.clan = clan;
        this.duration = duration;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public String getClan() {
        return clan;
    }
    
    public void setClan(String clan) {
        this.clan = clan;
    }

}
