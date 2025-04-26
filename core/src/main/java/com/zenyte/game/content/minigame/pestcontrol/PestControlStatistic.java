package com.zenyte.game.content.minigame.pestcontrol;

/**
 * @author Kris | 27. juuni 2018 : 15:02:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PestControlStatistic {

    private int activity;
    private int damageDealt;
    public PestControlStatistic(final int activity) {
        this.activity = activity;
    }

    public void incrementActivity(final int amount) {
        if (activity <= 0) {
            return;
        }
        if ((activity += amount) > PestControlUtilities.FULL_ACTIVITY_PERCENTAGE_VALUE) {
            activity = PestControlUtilities.FULL_ACTIVITY_PERCENTAGE_VALUE;
        }
    }

    public void decrementActivity() {
        if (activity > 0) {
            activity--;
        }
    }

    public void incrementDamageDealt(final int amount) {
        damageDealt += amount;
    }

    public int getActivity() {
        return activity;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

}
