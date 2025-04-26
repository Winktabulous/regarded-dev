package com.near_reality.game.coroutine

class GameCoroutineTimedState(private var ticks: Int) : GameCoroutineState<Unit> {

    override fun resume() = --ticks == 0

    override fun get() {}

}