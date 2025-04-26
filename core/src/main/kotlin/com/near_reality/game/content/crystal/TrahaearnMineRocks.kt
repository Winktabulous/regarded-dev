package com.near_reality.game.content.crystal

import com.zenyte.game.content.skills.mining.MiningDefinitions.OreDefinitions
import com.zenyte.game.world.`object`.ObjectId

/**
 * Represents rocks that can be mined in Trahaearn mine.
 *
 * @author Stan van der Bend
 */
enum class TrahaearnMineRocks(
    val level: Int,
    val xp: Int,
    val amount: Int,
    val ore: OreDefinitions,
    vararg val objectIds: Int,
) {
    GOLD(40, 65, 14, OreDefinitions.GOLD, ObjectId.ROCKS_36206),
    SOFT_CLAY(70, 5, 10, OreDefinitions.CLAY, ObjectId.ROCKS_36210),
    SILVER(20, 40, 8, OreDefinitions.SILVER, ObjectId.ROCKS_36205),
    IRON(15, 35, 26, OreDefinitions.IRON, ObjectId.ROCKS_36203),
    RUNITE(85, 125, 4, OreDefinitions.RUNITE, ObjectId.ROCKS_36209),
    MITHRIL(55, 80, 7, OreDefinitions.MITHRIL, ObjectId.ROCKS_36207),
    COAL(30, 50, 19, OreDefinitions.COAL, ObjectId.ROCKS_36204),
    ADAMANTITE(70, 95, 7, OreDefinitions.ADAMANTITE, ObjectId.ROCKS_36208);

    companion object {
        fun getAllRockObjectIds() = values().flatMap { it.objectIds.toList() }.toIntArray()
    }
}
