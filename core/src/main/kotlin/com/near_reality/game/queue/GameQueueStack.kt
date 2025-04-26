package com.near_reality.game.queue

import com.near_reality.game.coroutine.GameCoroutineTask
import kotlinx.coroutines.withContext

class GameQueueStack(
    private var currQueue: GameQueue? = null,
    private var currPriority: QueueType = QueueType.Weak,
    private val pendQueue: ArrayDeque<GameQueueBlock> = ArrayDeque()
) {

    constructor() : this(null)

    val size: Int
        get() = pendQueue.size + (if (currQueue != null) 1 else 0)

    val idle: Boolean
        get() = currQueue == null

    fun queue(type: QueueType, block: suspend () -> Unit) {
        if (!overtakeQueues(type)) {
            return
        }
        if (size >= MAX_ACTIVE_QUEUES) {
            pendQueue.removeLast()
        }
        val queueBlock = GameQueueBlock(block)
        pendQueue.add(queueBlock)
    }

    fun clear() {
        currQueue = null
        currPriority = QueueType.Weak
        pendQueue.clear()
    }

    fun processCurrent() {
        val queue = currQueue ?: return
        queue.task.cycle()
        if (queue.task.idle) {
            discardCurrent()
        }
    }

    fun pollPending() {
        if (currQueue != null) return
        if (pendQueue.isEmpty()) return
        val ctx = pendQueue.removeLast()
        val task = GameCoroutineTask()
        val block = suspend { withContext(task) { ctx.block() } }
        task.launch(block)
        currQueue = GameQueue(task)
    }

    fun discardCurrent() {
        currQueue = null
        /* only reset priority if no other queue is pending */
        if (pendQueue.isEmpty()) {
            currPriority = QueueType.Weak
        }
    }

    fun overtakeQueues(priority: QueueType): Boolean {
        if (priority == currPriority) {
            return true
        }
        if (!priority.overtake(currPriority)) {
            return false
        }
        if (priority != currPriority) {
            clear()
            currPriority = priority
        }
        return true
    }

    private fun QueueType.overtake(other: QueueType): Boolean = when (this) {
        QueueType.Normal -> other == QueueType.Weak
        QueueType.Strong -> true
        else -> false
    }

    private companion object {
        private const val MAX_ACTIVE_QUEUES = 2
    }

}