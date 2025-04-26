package com.near_reality.game.content.gauntlet.hunllef;

import com.zenyte.game.task.TickTask;

public abstract class HunleffTickTask extends TickTask {

    private final Hunllef hunllef;
    private int tick;

    public HunleffTickTask(Hunllef hunllef) {
        this.hunllef = hunllef;
    }

    @Override
    public final void run() {
        if (hunllef.isDead()){
            stop();
            return;
        }
        onTick(tick++);
    }

    public abstract void onTick(int tick);
}
