package com.near_reality.game.world.entity.npc.spawns

import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader

/**
 * @author Jire
 */
interface NPCSpawnLoaderMode {

    @Throws(Throwable::class)
    fun parse(loader: NPCSpawnLoader)

}