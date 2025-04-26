package com.near_reality.game.queue

import com.near_reality.game.coroutine.GameCoroutineTask

@JvmInline
value class GameQueue(val task: GameCoroutineTask)