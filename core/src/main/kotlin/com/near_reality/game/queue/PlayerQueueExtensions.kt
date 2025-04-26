package com.near_reality.game.queue

import com.zenyte.game.world.entity.player.Player

fun Player.weakQueue(block: suspend () -> Unit) = queueStack.queue(QueueType.Weak, block)

fun Player.normalQueue(block: suspend () -> Unit) = queueStack.queue(QueueType.Normal, block)

fun Player.strongQueue(block: suspend () -> Unit) = queueStack.queue(QueueType.Strong, block)