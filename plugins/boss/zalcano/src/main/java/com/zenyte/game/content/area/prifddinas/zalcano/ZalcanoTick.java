package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.task.TickTask;

public abstract class ZalcanoTick extends TickTask {

    protected ZalcanoInstance instance;

    public ZalcanoTick(ZalcanoInstance instance) {
        this.instance = instance;
    }
}
