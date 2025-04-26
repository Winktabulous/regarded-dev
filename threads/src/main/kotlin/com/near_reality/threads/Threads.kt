package com.near_reality.threads

import net.openhft.affinity.AffinityLock
import net.openhft.chronicle.core.OS
import net.openhft.chronicle.core.threads.EventLoop
import net.openhft.chronicle.threads.EventGroupBuilder
import net.openhft.chronicle.threads.Pauser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Jire
 */
object Threads {

    private val logger: Logger = LoggerFactory.getLogger(Threads::class.java)

    private val availableProcessors =
        1.coerceAtLeast(Runtime.getRuntime().availableProcessors())

    private const val DEFAULT_THREADS_PER_CORE = 1

    val threadsPerCore by lazy {
        if (OS.isWindows()) {
            try {
                var physicalCores = 0

                val exec: Process =
                    Runtime.getRuntime().exec("wmic CPU Get NumberOfCores /Format:List")
                exec.inputReader().use { reader ->
                    do {
                        val line = reader.readLine() ?: break
                        if (line.startsWith("NumberOfCores=")) {
                            physicalCores = line.substringAfter("NumberOfCores=").toIntOrNull() ?: continue
                            break
                        }
                    } while (true)
                }

                if (physicalCores > 0) {
                    return@lazy availableProcessors / physicalCores
                }
            } catch (e: Exception) {
                logger.error("Failed to determine threads-per-core on Windows OS", e)
            }
            DEFAULT_THREADS_PER_CORE
        } else AffinityLock.cpuLayout().threadsPerCore()
    }

    /**
     * Event loop dedicated for blocking I/O operations.
     */
    val io: EventLoop =
        EventGroupBuilder.builder()
            .withConcurrentThreadsNum(64.coerceAtLeast(availableProcessors))
            .withPauser(Pauser.balanced())
            .withName("io")
            .build()

    /**
     * Event loop for long-running operations, but not I/O.
     *
     * Handlers submitted to this loop will be treated with higher priority than I/O.
     * Ideally, handlers should not be blocking.
     */
    val slow: EventLoop =
        EventGroupBuilder.builder()
            .withConcurrentThreadsNum(1.coerceAtLeast(availableProcessors / 4))
            .withPauser(Pauser.yielding())
            .withName("slow")
            .build()

    fun preciseSleep(
        totalNanos: Long,
        targetBusyWaitingNanos: Long = 100_000_000
    ) {
        val startTime = System.nanoTime()

        // sleeping
        val sleepNanos = totalNanos - targetBusyWaitingNanos - 1_000_000 // extra millisecond because sleep expected
        if (sleepNanos > 0) {
            while (System.nanoTime() - startTime < sleepNanos) {
                Thread.sleep(0)
            }
        }

        // busy-waiting
        while (System.nanoTime() - startTime < totalNanos) {
            Thread.onSpinWait()
        }
    }

}