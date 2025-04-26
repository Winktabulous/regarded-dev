package com.near_reality.game.queue

import com.zenyte.game.world.entity.Entity

fun Entity.clearQueues() = queueStack.clear()