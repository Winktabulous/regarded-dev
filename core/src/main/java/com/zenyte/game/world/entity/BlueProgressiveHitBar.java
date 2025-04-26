package com.zenyte.game.world.entity;

/**
 * @author Kris | 18. nov 2017 : 6:02.57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BlueProgressiveHitBar extends HitBar {

    private int percentage;

    public BlueProgressiveHitBar(final int percentage) {
        this.percentage = percentage;
    }

    @Override
    public int getType() {
        return 7;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

}
