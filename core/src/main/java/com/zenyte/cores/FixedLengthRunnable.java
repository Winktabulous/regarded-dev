package com.zenyte.cores;

import java.util.concurrent.Future;

/**
 * A {@link Runnable} subtype intended to run for a fixed iteration period
 * in the context of a {@link java.util.concurrent.ScheduledExecutorService}.
 * @author David O'Neill
 */
public abstract class FixedLengthRunnable implements Runnable {

    private Future<?> future;

    /**
     * Iteratively runs {@code repeat()} until
     * it returns {@code false}, at which point
     * this runnable is cancelled from the executor.
     */
    @Override
    public final void run() {
        if(!repeat()) cancel();
    }

    /**
     * Defines the intended logic to be repeated, and
     * should return {@code false} when the runnable
     * should be cancelled and dequeued.
     * @return whether or not to run another iteration
     */
    public abstract boolean repeat();

    /**
     * Assign this runnables {@link Future} reference.
     * @param future - the future associated with this runnable
     */
    final void assignFuture(Future<?> future) {
        this.future = future;
    }

    /**
     * Unschedule the runnable, and ask the {@link SlowThreadPoolExecutor}
     * to purge the cancelled task from the pool's queue.
     */
    private void cancel() {
        future.cancel(false);
        CoresManager.purgeSlowExecutor();
    }

    /**
     *
     * Unschedule the runnable, and ask the {@link SlowThreadPoolExecutor}
     * to purge the cancelled task from the pool's queue.
     *
     * @param interrupt boolean indicating whether or not current execution
     *                  state should be killed regardless of whether or
     *                  not it has finished
     */
    public final void stopNow(boolean interrupt) {
        future.cancel(interrupt);
        CoresManager.purgeSlowExecutor();
    }
}
