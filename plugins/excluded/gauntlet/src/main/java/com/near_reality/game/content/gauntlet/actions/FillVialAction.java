package com.near_reality.game.content.gauntlet.actions;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;

public final class FillVialAction extends Action {

    private static final int VIAL = 23879;

    private static final Animation ANIMATION = new Animation(827);

    @Override
    public boolean start() {
        return player.getInventory().containsItem(VIAL);
    }

    @Override
    public boolean process() {
        return player.getInventory().containsItem(VIAL);
    }

    @Override
    public int processWithDelay() {
        player.setAnimation(ANIMATION);

        player.getInventory().deleteItem(VIAL, 1);
        player.getInventory().addItem(VIAL + 1, 1);
        player.sendMessage("You fill a crystal vial with water.");

        return 1;
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }
}
