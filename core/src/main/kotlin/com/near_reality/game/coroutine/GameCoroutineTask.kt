package com.near_reality.game.coroutine

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.startCoroutine

class GameCoroutineTask(
    private var coroutine: GameCoroutine<out Any>? = null
) : AbstractCoroutineContextElement(GameCoroutineTask) {

    companion object Key : CoroutineContext.Key<GameCoroutineTask>

    val idle: Boolean
        get() = coroutine == null

    fun delay(ticks: Int, continuation: Continuation<Unit>) {
        val condition = GameCoroutineTimedState(ticks)
        coroutine = GameCoroutine(continuation, condition)
    }

    fun delay(predicate: () -> Boolean, continuation: Continuation<Unit>) {
        val condition = GameCoroutinePredicateState(predicate)
        coroutine = GameCoroutine(continuation, condition)
    }

    fun cycle() {
        val coroutine = coroutine ?: return
        val resume = coroutine.resume()
        if (resume && this.coroutine === coroutine) {
            this.coroutine = null
        }
    }

    fun cancel(): Nothing {
        coroutine = null
        throw CancellationException()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> submit(event: T) {
        val coroutine = coroutine as? GameCoroutine<Any> ?: return
        coroutine.submit(event)
    }

    fun launch(block: suspend () -> Unit) {
        block.startCoroutine(DefaultGameCoroutineContinuation)
    }

}