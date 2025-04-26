package com.near_reality.game.coroutine

interface GameCoroutineState<T> {

    fun resume(): Boolean

    fun get(): T

}