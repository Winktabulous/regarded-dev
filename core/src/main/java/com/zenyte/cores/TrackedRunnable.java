package com.zenyte.cores;

import java.util.UUID;
import java.util.concurrent.Future;

/**
 * A {@link Runnable} subtype intended to run for a dynamic iteration period
 * in the context of a {@link java.util.concurrent.ScheduledExecutorService}.
 * @author David O'Neill
 */
public abstract class TrackedRunnable implements Runnable {

    private final String trackingKey;

    public TrackedRunnable() {
        trackingKey = UUID.randomUUID().toString();
    }

    /**
     * Returns this runnables tracing key. This key
     * should be supplied to the {@link com.rs.cores.CoresManager.ServiceProvider}
     * to cancel the {@link Future} associated with this runnable.
     * @return the tracking key for this runnable
     */
    public String getTrackingKey() {
        return trackingKey;
    }

    public abstract void run();

}
