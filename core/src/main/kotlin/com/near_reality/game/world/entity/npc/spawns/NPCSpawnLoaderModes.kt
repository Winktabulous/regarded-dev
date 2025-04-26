package com.near_reality.game.world.entity.npc.spawns

import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader

/**
 * @author Jire
 */
enum class NPCSpawnLoaderModes(private val parse: () -> Unit) : NPCSpawnLoaderMode {

    JSON(NPCSpawnLoader::parseJSON),
    JSON_AND_GENERATE_KTS({
        NPCSpawnLoader.parseJSON()
        OldSpawnKTSGenerator().generateAndWriteKTS()
    }),
    SKIP({});

    override fun parse(loader: NPCSpawnLoader) = parse()

}