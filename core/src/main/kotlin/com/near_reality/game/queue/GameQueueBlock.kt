package com.near_reality.game.queue

@JvmInline
value class GameQueueBlock(val block: suspend () -> Unit)