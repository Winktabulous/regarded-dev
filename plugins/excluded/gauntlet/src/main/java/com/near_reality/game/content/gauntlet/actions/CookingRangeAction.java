package com.near_reality.game.content.gauntlet.actions;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Andys1814.
 * @since 1/28/2022.
 */
public final class CookingRangeAction extends Action {

    private final boolean corrupted;

    public CookingRangeAction(boolean corrupted) {
        this.corrupted = corrupted;
    }

    private static final Animation ANIMATION = new Animation(896);

    private static final int RAW_PADDLEFISH = 23872;

    private static final int COOKED_PADDLEFISH = 23874;

    private static final int BURNT_PADDLEFISH = 23873;

    @Override
    public boolean start() {
        if (!player.getInventory().containsItem(RAW_PADDLEFISH)) {
            player.sendMessage("You have nothing to cook at the moment.");
            return false;
        }

        cook();
        delay(1);
        return true;
    }

    @Override
    public boolean process() {
        return player.getInventory().containsItem(RAW_PADDLEFISH);
    }

    @Override
    public int processWithDelay() {
        cook();
        return 0;
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }

    public void cook() {
        player.setAnimation(ANIMATION);

        boolean burn = Utils.random(100) < Math.min(48 - player.getSkills().getLevel(SkillConstants.COOKING), 34);

        player.getInventory().deleteItem(RAW_PADDLEFISH, 1);
        player.getInventory().addItem(burn ? BURNT_PADDLEFISH : COOKED_PADDLEFISH, 1);
        player.sendMessage("You " + (burn ? "accidentally burn the" : "successfully cook a") + " paddlefish.");

        if (!burn) {
            player.getSkills().addXp(SkillConstants.COOKING, 15);
        }
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }
}
