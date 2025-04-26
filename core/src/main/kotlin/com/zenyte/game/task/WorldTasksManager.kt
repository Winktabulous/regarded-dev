package com.zenyte.game.task

import com.near_reality.game.util.MessagePassingQueues.drain
import com.zenyte.game.world.World
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.jctools.queues.MessagePassingQueue
import org.jctools.queues.MpscArrayQueue
import org.jctools.queues.SpscArrayQueue
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Used to schedule [WorldTask]s.
 *
 * @author Jire
 */
object WorldTasksManager {

    /**
     * The default initial task delay, in ticks.
     */
    const val DEFAULT_INITIAL_DELAY = 0

    /**
     * Indicates that the task should not be rescheduled after its initial delay.
     */
    const val NO_PERIOD_DELAY = -1

    /**
     * The capacity of the tasks queue.
     */
    private const val CAPACITY = (World.PLAYER_LIST_CAPACITY + World.NPC_CAPACITY) * 256

    /**
     * Used for logging via SLF4J API.
     */
    private val logger: Logger = LoggerFactory.getLogger(WorldTasksManager::class.java)

    /**
     * A lock used for global synchronization of the task manager.
     */
    private val lock = Any()

    /**
     * The pending tasks, which are first added to the [active] tasks during processing.
     */
    private val pending: MessagePassingQueue<WorldTaskContinuation> =
        MpscArrayQueue(CAPACITY)

    /**
     * The active tasks, which will be processed in the current tick.
     */
    private val active: MessagePassingQueue<WorldTaskContinuation> =
        SpscArrayQueue(CAPACITY)

    /**
     * Maps [WorldTask] to its [WorldTaskContinuation] continuation.
     */
    private val continuations: Object2ObjectMap<WorldTask, WorldTaskContinuation> =
        Object2ObjectOpenHashMap(CAPACITY)

    @JvmStatic
    fun processTasks() {
        /* First, drain the pending tasks into the active. */
        drain(pending, CAPACITY) {
            val added = active.offer(it)
            if (added) {
                it.map()
            } else if (logger.isWarnEnabled) {
                val activeSize: Int
                synchronized(lock) { // need to synchronize for `active` thread-safety.
                    activeSize = active.size()
                }
                logger.warn(
                    "Unable to drain `pending` ({} size) into `active` ({} size)",
                    pending.size(),
                    activeSize
                )
            }

            added
        }

        /* Then process the active tasks, re-adding back to `pending` if it should be continued. */
        synchronized(lock) { // original Zenyte locks for the entire processing duration. Could be optimized out.
            drain(active, CAPACITY) {
                val continues = it.continues()
                if (!continues) {
                    it.unmap()
                    return@drain true
                }

                val added = pending.offer(it)
                if (!added) {
                    if (logger.isWarnEnabled) {
                        logger.warn(
                            "Unable to drain `active` ({} size) into `pending` ({} size)",
                            active.size(),
                            pending.size()
                        )
                    }

                    it.unmap()
                }
                added
            }
        }
    }

    /**
     * @param task The [WorldTask] to run.
     *
     * @param initialDelay The initial delay, in ticks, to first run the task.
     * @param periodDelay The continued delay, in ticks, to reschedule the task after the [initialDelay] has passed,
     * or [NO_PERIOD_DELAY] to not reschedule after the initial delay.
     *
     * @return `true` when the task was successfully added to the manager's queue, `false` otherwise.
     */
    @JvmStatic
    @JvmOverloads
    fun schedule(
        task: WorldTask,

        initialDelay: Int = DEFAULT_INITIAL_DELAY,
        periodDelay: Int = NO_PERIOD_DELAY
    ): Boolean {
        if (initialDelay < 0) {
            throw IllegalArgumentException("`initialDelay` must be at least 0")
        }
        if (periodDelay != NO_PERIOD_DELAY && periodDelay < 0) {
            throw IllegalArgumentException(
                "`periodDelay` must be at least 0, " +
                        "if not unspecified (use `NO_PERIOD_DELAY`)"
            )
        }

        val continuation = WorldTaskContinuation(
            task,

            initialDelay,
            periodDelay
        )

        val added = pending.offer(continuation)
        if (!added && logger.isWarnEnabled) {
            logger.warn(
                "Unable to add to `pending` ({} size)",
                pending.size()
            )
        }
        return added
    }

    /**
     * @param initialDelay The initial delay, in ticks, to first run the task.
     * @param periodDelay The continued delay, in ticks, to reschedule the task after the [initialDelay] has passed,
     * or [NO_PERIOD_DELAY] to not reschedule after the initial delay.
     *
     * @param task The [WorldTask] to run.
     *
     * @return `true` when the task was successfully added to the manager's queue, `false` otherwise.
     */
    @JvmStatic
    fun schedule(
        initialDelay: Int = DEFAULT_INITIAL_DELAY,
        periodDelay: Int = NO_PERIOD_DELAY,

        task: WorldTask
    ): Boolean = schedule(task, initialDelay, periodDelay)

    /**
     * @param task The [WorldTask] to run.
     *
     * @param initialDelay The initial delay, in ticks, to first run the task.
     * @param periodDelay The continued delay, in ticks, to reschedule the task after the [initialDelay] has passed,
     * or [NO_PERIOD_DELAY] to not reschedule after the initial delay.
     *
     * @return `true` when the task executed immediately, or was successfully added to the manager's queue,
     * `false` otherwise.
     */
    @JvmStatic
    @JvmOverloads
    fun scheduleOrExecute(
        task: WorldTask,

        initialDelay: Int = DEFAULT_INITIAL_DELAY,
        periodDelay: Int = NO_PERIOD_DELAY
    ): Boolean {
        return if (initialDelay < 0) {
            // original Zenyte locks during task run, although this may not be necessary.
            synchronized(lock) {
                task.run()
            }
            true
        } else schedule(task, initialDelay, periodDelay)
    }

    /**
     * The total count of tasks.
     */
    @JvmStatic
    val count: Int
        get() {
            val pending = this.pending.size()

            val active: Int
            synchronized(lock) {
                active = this.active.size()
            }

            return pending + active
        }

    /**
     * Stops the specified [WorldTask].
     *
     * @param worldTask The task to stop.
     *
     * @return whether the task was stopped.
     */
    @JvmStatic
    fun stop(worldTask: WorldTask): Boolean {
        synchronized(lock) {
            val continuation = continuations[worldTask] ?: return false
            continuation.stop()
            return true
        }
    }

    /**
     * Stores information about the current tasks' continuation.
     */
    private class WorldTaskContinuation(
        val task: WorldTask,

        val initialDelay: Int,
        val periodDelay: Int
    ) {

        private var stopped = false

        private var countdown = initialDelay

        fun stop() {
            stopped = true
        }

        fun continues(): Boolean {
            if (stopped) {
                return false
            }
            if (countdown > 0) {
                countdown--
                return true
            }

            try {
                task.run()
            } catch (e: Exception) {
                logger.error("Failed to execute task", e)
                stop()
                return false
            }

            if (periodDelay == NO_PERIOD_DELAY) {
                stop()
                return false
            }
            countdown = periodDelay
            return true
        }

        fun map() {
            val existing = continuations.put(task, this)
            if (existing != null && existing != this) {
                logger.warn(
                    "Overridden existing task continuation ({}) that didn't match this entry ({})",
                    existing, this
                )
            }
        }

        fun unmap() {
            val removed = continuations.remove(task)
            if (this != removed) {
                logger.warn(
                    "Removed task continuation ({}) didn't match existing entry ({})",
                    removed, this
                )
            }
        }

    }

}