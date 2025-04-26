package com.near_reality.game.coroutine

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class GameCoroutine<T : Any>(
    private val cont: Continuation<T>,
    private val state: GameCoroutineState<T>
) {

    fun resume(): Boolean {
        if (!state.resume()) {
            return false
        }
        val value = state.get()
        cont.resume(value)
        return true
    }

    fun submit(value: T) {
        if (state !is GameCoroutineDeferValueState) return
        if (state.type == value::class) {
            state.set(value)
        }
    }

}