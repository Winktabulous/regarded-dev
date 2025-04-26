package com.near_reality.game.queue

import com.near_reality.game.coroutine.GameCoroutineTask
import kotlinx.coroutines.withContext
import org.jctools.queues.MessagePassingQueue
import org.jctools.queues.SpscLinkedQueue

class GameQueueList(
    private val queues: MutableList<GameQueue> = ArrayDeque(),
    private val pending: MessagePassingQueue<GameQueueBlock> = SpscLinkedQueue()
) : List<GameQueue> by queues {

    private val drainPending = MessagePassingQueue.Consumer<GameQueueBlock> { ctx ->
        val task = GameCoroutineTask()
        val block = suspend { withContext(task) { ctx.block() } }
        val queue = GameQueue(task)
        task.launch(block)
        if (!queue.task.idle) {
            queues.add(queue)
        }
    }

    fun queue(block: suspend () -> Unit) {
        val queueBlock = GameQueueBlock(block)
        pending.offer(queueBlock)
    }

    fun cycle() {
        cycleQueues()
        addPending()
    }

    private fun cycleQueues() {
        queues.forEach { it.task.cycle() }
        queues.removeIf { it.task.idle }
    }

    private fun addPending() {
        pending.drain(drainPending)
    }

}