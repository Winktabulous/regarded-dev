package com.zenyte.game.world.entity;

/**
 * @author Kris | 19. jaan 2018 : 23:51.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BeigeProgressiveHitBar extends HitBar {

    private int percentage;

    public BeigeProgressiveHitBar(final int percentage) {
        this.percentage = percentage;
    }

    @Override
    public int getType() {
        return 8;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

}
