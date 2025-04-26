package com.near_reality.game.coroutine

import com.zenyte.game.world.entity.player.Player
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

private val CoroutineContext.task: GameCoroutineTask
    get() = get(GameCoroutineTask) ?: error(
        """Game coroutine task has not been set.
        Construct one and call the suspend block with `withContext(${GameCoroutineTask::class.simpleName})`,
        or use the appropriate entity queue method (i.e ${Player::class.simpleName}::normalQueue)
        """
    )

suspend fun delay(ticks: Int = 1) {
    if (ticks <= 0) return
    return suspendCoroutineUninterceptedOrReturn {
        it.context.task.delay(ticks, it)
        COROUTINE_SUSPENDED
    }
}

suspend fun delay(predicate: () -> Boolean) {
    if (predicate()) return
    return suspendCoroutineUninterceptedOrReturn {
        it.context.task.delay(predicate, it)
        COROUTINE_SUSPENDED
    }
}

suspend fun stop(): Nothing = coroutineContext.task.cancel()