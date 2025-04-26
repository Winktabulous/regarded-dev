package com.zenyte.game.util;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.masks.Animation;
import mgi.types.config.AnimationDefinitions;

public class AnimationUtil {
    /**
     * Calculates the additional delay of the animation so it will be synchronized within game ticks, which means the animation ends exactly
     * when the next game cycle is initiated. The delay is always ceiled, so if animation duration is for example 620ms, the delay returned
     * will push the animation's "duration" to 1200ms.
     *
     * @param id
     *            the id of the animation.
     * @return the additional delay of the animation to synchronize it with game ticks.
     */
    public static final int getSynchronizedAnimationDelay(final int id) {
        final AnimationDefinitions definitions = AnimationDefinitions.get(id);
        if (definitions == null) {
            return 0;
        }
        final int duration = definitions.getDuration();
        final float remainderCycles = (duration - ((int) (duration / GameConstants.TICK) * GameConstants.TICK)) / GameConstants.CLIENT_CYCLE;
        return (int) (GameConstants.CYCLES_PER_TICK - remainderCycles);
    }

    public static int getCeiledDuration(Animation animation) {
        final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
        if (defs == null) {
            return 0;
        }
        final int duration = (int) (defs.getDuration() + (GameConstants.CLIENT_CYCLE * animation.getDelay()));
        final float remainder = duration % GameConstants.TICK;
        if (remainder > 0) {
            return (int) (duration + (GameConstants.TICK - remainder));
        }
        return duration;
    }

    public static int getDuration(Animation animation) {
        final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
        if (defs == null) {
            return 0;
        }
        return (int) (defs.getDuration() + (GameConstants.CLIENT_CYCLE * animation.getDelay()));
    }
}
