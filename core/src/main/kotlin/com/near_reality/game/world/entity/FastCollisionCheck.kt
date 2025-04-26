package com.near_reality.game.world.entity

import com.zenyte.game.util.CollisionUtil
import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.npc.NPC

fun collides(entity: AbstractEntity, iterator: Iterator<Entity>, x: Int, y: Int): Boolean {
    while(iterator.hasNext()) {
        val next = iterator.next()
        if (next == entity || next.isFinished || (next is NPC && !next.isEntityClipped))
            continue
        if (CollisionUtil.collides(next.x, next.y, next.size, x, y, 1))
            return true
    }
    return false
}
