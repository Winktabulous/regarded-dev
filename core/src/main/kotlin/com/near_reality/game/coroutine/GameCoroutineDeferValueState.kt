package com.near_reality.game.coroutine

import kotlin.reflect.KClass

class GameCoroutineDeferValueState<T : Any>(
    val type: KClass<T>,
    var resume: Boolean = false
) : GameCoroutineState<T> {

    private lateinit var value: T

    fun set(value: T) {
        this.value = value
        this.resume = true
    }

    override fun resume(): Boolean {
        return resume
    }

    override fun get(): T = value

}