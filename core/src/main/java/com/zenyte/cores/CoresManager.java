package com.zenyte.cores;

import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.player.login.LoginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * The CoresManager is responsbile for initializing thread behaviour.
 * <p>
 * The key things to remember about the game engine are as follows:<br/>
 * <br/>
 * <p>
 * The main thread handles game-tick based tasks, such as {@code WorldTask}s and general game actions. Therefore, if
 * performing a scheduled
 * or delayed execution based on game ticks, use the {@code WorldTasksManager.schedule(...)} approach.<br/>
 * <br/>
 * <p>
 * The {@code slowExecutor} manages a pool of threads dedicated to running either continuously repeated tasks, or
 * {@link FixedLengthRunnable} objects. In either of these cases, the frequency of the {@code run()} call is not
 * bound to game ticks - it
 * runs for the specified interval with a specified time unit.<br/>
 * <br/>
 * <p>
 * The {@code fastExecutor} manages a pool of threads dedicated to running single-execution {@link Runnable}s
 * immediately after they are
 * submitted for exeuction. Like the {@code slowExecutor}, the start delay of the {@code run()} call is not bound to
 * game ticks - it will
 * run as soon as the thread pool supplies a thread to run it.<br/>
 * <br/>
 * <p>
 * "then it (the {@code fastExecutor}) shouldn't carry the downfalls of the timer based system. "<br/>
 *
 * <pre>
 *     - Noele, when (indirectly) talking about the inconsistencies
 *       of mixing threading APIs in a fully multi-threaded system.
 * </pre>
 * <p>
 * <p>
 * Exactly! But, with the way we have implemented it, it (the {@code fastExecutor}) doesn't! The underlying thread
 * pool executor objects
 * ({@code slowExecuter} and {@code fastExecutor}) are of a different type; one is a subclass of a
 * {@link ScheduledExecutorService}
 * ({@code slowExecuter}) and the other is a subclasss of {@link ExecutorService} ({@code fastExecutor}).<br/>
 * <br/>
 * <p>
 * The {@code fastExecutor} can only call {@code execute(Runnable r)}, {@code call(Callable c)}, and {@code submit
 * (Runnable r)}, all of
 * which do the same thing: run a {@link Runnable} once and only once, and as soon as a thread is supplied from the
 * thread pool.<br/>
 * <br/>
 * <p>
 * The {@code slowExecuter} also has {@code execute(Runnable r)}, but also has things like {@code schedule(...)},
 * {@code scheduleWithFixedDelay(...)}, ... , which allow it to repeat tasks, or start tasks after a delay.<br/>
 * <br/>
 * <p>
 * These methods might be common knowledge, but it is important to stress the fundamental reason that both the {@code
 * slowExecuter} and
 * {@code fastExecutor} exist; each has a separate thread pool maintaining them. Unique thread pools mapped to a
 * unique type of threaded
 * service. Good for organization, good for resource management.<br/>
 * <br/>
 * <p>
 * Furthermore, with the {@link ServiceProvider}, all of this functionality is wrapped in a class which is
 * responsible for choosing the
 * correct executor service to use.<br/>
 * <br/>
 * <p>
 * For those of you TL;DR nerds: we no longer use a {@link java.util.Timer} object for the {@code fastExecutor}, as
 * its functionality will
 * be deprecated in Java 9, and quite frankly, because it is ancient history when compared to the {@code Executors}
 * framework. We instead
 * use a custom manager called a {@code ServiceProvider} to use the executor services.<br/>
 * <br/>
 * <p>
 * What you used to do with a {@code TimerTask}:<br/>
 *
 * <pre>
 *     CoresManager.fastExecutor.scheduleAtFixedRate(new TimerTask {
 *     		int someStuff;
 *     		boolean stop;
 *
 *     		public void run() { if(stop) cancel(); }
 *
 *     }, delay, freq);
 * </pre>
 * <p>
 * What we do with the {@link ServiceProvider}
 *
 * <pre>
 * CoresManager.getServiceProvider().scheduleFixedLengthTask(new FixedLengthRunnable() {
 * 	boolean stop;
 *
 * 	public boolean repeat() {
 * 		// do logic
 * 		if (someCondition)
 * 			stop = true;
 * 		else
 * 			stop = false;
 * 		return stop;
 *    }
 * }, 0, 1, TimeUnit.SECONDS);
 * </pre>
 */
public final class CoresManager {
    private static final Logger log = LoggerFactory.getLogger(CoresManager.class);
    public static WorldThread worldThread;
    public static volatile boolean shutdown;
    public static ScheduledExecutorService slowExecutor;
    private static ServiceProvider serviceProvider;
    private static LoginManager loginManager;

    private CoresManager() {
    }

    /**
     * Returns the core's {@code ServiceProvider} used for accessing the executor services.
     *
     * @return the core {@link ServiceProvider}
     */
    public static ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public static void closeServices() {
        try {
            slowExecutor.shutdown();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void join() {
        try {
            slowExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        try {
            loginManager.getThread().join(5000);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

    public static final String WORLD_THREAD_NAME = "World Thread";

    public static void init() {
        worldThread = new WorldThread(WORLD_THREAD_NAME);
        slowExecutor = new SlowThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                new SlowThreadFactory(new SlowThreadHandler()),
                "Slow thread pool executor");
        serviceProvider = new ServiceProvider(false);
        loginManager = new LoginManager();
    }

    public static WorldThread startWorldThread() {
        worldThread.start();
        return worldThread;
    }

    static void purgeSlowExecutor() {
        ((SlowThreadPoolExecutor) slowExecutor).purge();
    }

    public static boolean isShutdown() {
        return shutdown;
    }

    public static void setShutdown(boolean shutdown) {
        CoresManager.shutdown = shutdown;
    }

    public static LoginManager getLoginManager() {
        return loginManager;
    }

    /**
     * Serves as a centralized hub for executor services in the context of the game engine. New developers should not
     * have to know which
     * executor to use, but should rather be able to call wrapper methods with generic names and descriptions, and let
     * the
     * {@code ServiceProvider} choose the correct {@link ExecutorService};
     *
     * @author David O'Neill
     */
    public static class ServiceProvider {
        private final Map<String, Future<?>> trackedFutures;
        private final boolean verbose;

        private ServiceProvider(final boolean verbose) {
            trackedFutures = new ConcurrentHashMap<>();
            this.verbose = verbose;
            log.info("ServiceProvider active and waiting for requests.");
        }

        /**
         * Schedules a {@code Runnable} to be executed after the supplied start delay, and continuously executed
         * thereafter at some
         * specified frequency. This method should be used when there is no intention of stopping the task before
         * server shutdown.<br/>
         * The start delay and repetition frequency time unit must be supplied.
         *
         * @param r          a {@link Runnable} to repeat
         * @param startDelay time delay before execution begins
         * @param delayCount frequency at which the {@code run()} method is called.
         * @param unit       the specified time unit
         */
        public void scheduleRepeatingTask(final Runnable r, final long startDelay, final long delayCount,
                                          final TimeUnit unit) {
            CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("", e);
                }
            }, startDelay, delayCount, unit);
        }

        /**
         * Schedules a {@code Runnable} to be executed after the supplied start delay, and continuously executed
         * thereafter at some
         * specified frequency. This method should be used when there is no intention of stopping the task before
         * server shutdown.<br/>
         * The start delay and repetition frequency time unit is assumed to be {@link TimeUnit#SECONDS}.
         *
         * @param r          a {@link Runnable} to repeat
         * @param startDelay time delay before execution begins
         * @param delayCount frequency at which the {@code run()} method is called.
         */
        public void scheduleRepeatingTask(final Runnable r, final long startDelay, final long delayCount) {
            CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("", e);
                }
            }, startDelay, delayCount, TimeUnit.SECONDS);
        }

        /**
         * Schedules a {@link FixedLengthRunnable} to be executed after the supplied start delay, and continuously
         * executed thereafter until
         * {@link FixedLengthRunnable#repeat()} returns false. This method should be used when there is absolute
         * certainty the task will
         * stop executing based on a future condition.<br/>
         * The start delay and repetition frequency time unit must be supplied.
         *
         * @param r          a {@link FixedLengthRunnable} to repeat
         * @param startDelay time delay before execution begins
         * @param delayCount frequency at which the {@code run()} method is called.
         * @param unit       the specified time unit
         */
        public void scheduleFixedLengthTask(final FixedLengthRunnable r, final long startDelay, final long delayCount
                , final TimeUnit unit) {
            final Future<?> f = CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("", e);
                }
            }, startDelay, delayCount, unit);
            r.assignFuture(f);
        }

        /**
         * Schedules a {@link FixedLengthRunnable} to be executed after the supplied start delay, and continuously
         * executed thereafter until
         * {@link FixedLengthRunnable#repeat()} returns false. This method should be used when there is absolute
         * certainty the task will
         * stop executing based on a future condition.<br/>
         * The start delay and repetition frequency time unit is assumed to be {@link TimeUnit#SECONDS}.
         *
         * @param r          a {@link FixedLengthRunnable} to repeat
         * @param startDelay time delay before execution begins
         * @param delayCount frequency at which the {@code run()} method is called.
         */
        public void scheduleFixedLengthTask(final FixedLengthRunnable r, final long startDelay,
                                            final long delayCount) {
            final Future<?> f = CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("", e);
                }
            }, startDelay, delayCount, TimeUnit.SECONDS);
            r.assignFuture(f);
        }

        /**
         * Schedules a {@link TrackedRunnable} to be executed after the supplied start delay, and continuously
         * executes it thereafter at
         * some specified frequency. Furthermore, the associated {@link Future} is registered with the {@code
         * ServiceProvider} via the
         * runnables tracking key. The {@link Future} can then be accessed with the key at a later time. This method
         * should be used when the
         * task will not necessarily be cancelled after a fixed iteration period, but may need to be shutdown at a
         * later, unknown time. In
         * order to retrieve the tracking key, you must have a reference to the {@link TrackedRunnable}, so using an
         * anonymous first
         * argument is discouraged.<br/>
         * If the String key supplied is already registered with the {@code ServiceProvider}, the task will NOT be
         * scheduled!<br/>
         * The start delay and repetition frequency time unit must be supplied.
         *
         * @param r          a {@link Runnable} to repeat
         * @param startDelay time delay before execution begins
         * @param delayCount frequency at which the {@code run()} method is called.
         * @param unit       the specified time unit
         */
        public void scheduleAndTrackRepeatingTask(final TrackedRunnable r, final long startDelay,
                                                  final long delayCount, final TimeUnit unit) {
            if (trackedFutures.containsKey(r.getTrackingKey())) {
                System.err.println(log("Attempted to enqueue Future to tracking map, but duplicate key was found. " +
                        "Aborting."));
                return;
            }
            final Future<?> future = CoresManager.slowExecutor.scheduleWithFixedDelay(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("", e);
                }
            }, startDelay, delayCount, unit);
            trackedFutures.put(r.getTrackingKey(), future);
            if (verbose) {
                log.info(log("Tracking new future with key: " + r.getTrackingKey()));
            }
        }

        /**
         * Attempts to retrieve a {@link Future} mapped to the supplied key. If the {@link Future} is present in the
         * {@code ServiceProvider}
         * mapping, it will be cancelled and purged from the executor pool.
         *
         * @param key       the String key (acquired via {@link TrackedRunnable#getTrackingKey()} to lookup a mapped
         *                  {@link Future}
         * @param interrupt whether or not the executor service should stop the current execution of the
         *                  {@link Future}'s associated
         *                  {@link Runnable} if an execution is in progress.
         */
        public void cancelTrackedTask(final String key, final boolean interrupt) {
            final Future<?> future = trackedFutures.remove(key);
            if (future != null) {
                future.cancel(interrupt);
                CoresManager.purgeSlowExecutor();
                if (verbose) {
                    log.info(log("Cancelled future with key: " + key));
                }
            }
        }

        /**
         * Schedules a {@code Runnable} for a one-time execution, but only after a specified start delay. The start
         * delay time unit must be
         * supplied.
         *
         * @param r          a {@link Runnable} to execute once
         * @param startDelay time delay before execution begins
         * @param unit       the specified time unit
         */
        public void executeWithDelay(final Runnable r, final long startDelay, final TimeUnit unit) {
            CoresManager.slowExecutor.schedule(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("", e);
                }
            }, startDelay, unit);
        }

        /**
         * Schedules a {@code Runnable} for a one-time execution, but only after a specified start delay. The start
         * delay time unit is
         * "ticks" by default, meaning units of 600ms. Calling {@code executeWithDelay(() -> stuff(), 2);} would
         * execute {@code stuff()}
         * after 2 ticks = 600 ms * 2 = 1200 ms.
         *
         * @param r     a {@link Runnable} to execute once
         * @param ticks the time delay in ticks before execution begins
         */
        public void executeWithDelay(final Runnable r, final int ticks) {
            CoresManager.slowExecutor.schedule(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("Exception loading game", e);
                }
            }, ticks * 600L, TimeUnit.MILLISECONDS);
        }

        /**
         * Immediately (as soon as a thread from the thread pool is provided) performs a one-time exeuction of a
         * supplied {@code Runnable}.
         *
         * @param r a {@link Runnable} to execute once
         */
        public void executeNow(final Runnable r) {
            CoresManager.slowExecutor.execute(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("", e);
                }
            });
        }

        public Future<?> submit(final Runnable r) {
            return CoresManager.slowExecutor.submit(() -> {
                try {
                    r.run();
                } catch (final Exception e) {
                    log.error("", e);
                }
            });
        }

        private String log(final String message) {
            final String prefix = "[Service Provider] => ";
            return prefix + message;
        }
    }
}
