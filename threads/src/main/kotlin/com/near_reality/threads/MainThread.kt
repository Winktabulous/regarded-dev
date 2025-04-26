package com.near_reality.threads

import net.openhft.affinity.AffinityLock

/**
 * Looping main thread with core-affinity and highly accurate cycle delay.
 *
 * @author Jire
 *
 * @property cycleNanos Interval of a cycle in nanoseconds.
 * @property minSleepNanos The minimum amount of nanoseconds to consider actually sleeping the thread.
 * @property targetCpuId Target CPU ID, starting at 0. Hyper-threads are considered CPUs.
 */
abstract class MainThread
@JvmOverloads
constructor(
    name: String,
    private val cycleNanos: Long = 600 * 1000 * 1000, // 600ms
    private val minSleepNanos: Long = 1 * 1000 * 1000, // 1ms
    private val targetCpuId: Int = Threads.threadsPerCore,
    priority: Int = MAX_PRIORITY,
) : Thread(name) {

    @Volatile
    var running = false

    init {
        this.priority = priority
    }

    /**
     * Performs a cycle.
     *
     * Assumed not to throw exceptions. If one is thrown, the thread will stop.
     */
    abstract fun cycle()

    override fun run() {
        val affinityLock: AffinityLock? =
            // only acquire lock if we have at least 2 processors (threads)
            if (System.getProperty("os.arch") != "aarch64" && Runtime.getRuntime().availableProcessors() > 1)
                AffinityLock.acquireLock(targetCpuId)
            else null
        try {
            running = true
            while (running && !interrupted()) {
                val startTime = System.nanoTime()

                cycle()

                val endTime = System.nanoTime()

                val elapsedNanos = endTime - startTime
                val sleepNanos = cycleNanos - elapsedNanos
                if (sleepNanos >= minSleepNanos) {
                    Threads.preciseSleep(sleepNanos)
                }
            }
        } finally {
            affinityLock?.release()
        }
    }

}
