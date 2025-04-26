package com.zenyte.game.content.boss.abyssalsire.tentacles

import java.util.*

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalTentacleQueue {

    private val queue: Queue<AbyssalTentacleQueueAction> = LinkedList()

    operator fun invoke(action: () -> Unit) = queue.add(AbyssalTentacleQueueAction(action))

    operator fun invoke(blockTicks: Int) = queue.add(AbyssalTentacleQueueAction(blockTicks = blockTicks))

    fun clear() = queue.clear()

    fun process(): Boolean {
        while (!queue.isEmpty()) {
            val action = queue.peek()

            if (--action.blockTicks > 0) return true

            action.run?.invoke()
            queue.remove()
        }

        return false
    }

}