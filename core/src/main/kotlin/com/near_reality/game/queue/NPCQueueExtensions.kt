package com.near_reality.game.queue

import com.zenyte.game.world.entity.npc.NPC

fun NPC.queue(block: suspend () -> Unit) = queueStack.queue(QueueType.Strong, block)