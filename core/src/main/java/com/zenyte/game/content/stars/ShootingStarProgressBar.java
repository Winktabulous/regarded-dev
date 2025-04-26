package com.zenyte.game.content.stars;

import com.zenyte.game.world.entity.HitBar;

/**
 * @author Andys1814
 */
public final class ShootingStarProgressBar extends HitBar {

    private int percentage;

    public ShootingStarProgressBar(final int percentage) {
        this.percentage = percentage;
    }

    @Override
    public int getType() {
        return 1;
    }

    public int getPercentage() {
        return percentage / 2;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

}
