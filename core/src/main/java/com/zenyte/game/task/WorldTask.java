package com.zenyte.game.task;

@FunctionalInterface
public interface WorldTask extends Runnable {

    default void stop() {
        defaultStop(this);
    }

    static void defaultStop(final WorldTask worldTask) {
        WorldTasksManager.stop(worldTask);
    }

}
